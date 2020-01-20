package uniupo.gaborgalazzo.studentamqpclient.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uniupo.gaborgalazzo.studentamqpclient.model.Payload;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class MqttSingleQueue {

    @Value("${mqtt.proxy.basetopic}")
    private String baseTopic;


    private final ObjectMapper objectMapper;
    private final IMqttClient mqttClient;
    private final HashMap<String, Payload> monitorMap;
    private final String clientId = UUID.randomUUID().toString();
    private boolean init = false;

    private final AtomicReference<String> resp = new AtomicReference<>();


    public MqttSingleQueue(
            ObjectMapper objectMapper,
            @Value("${mqtt.hostname}") String hostname,
            @Value("${mqtt.port}") int port
    ) throws MqttException
    {

        this.mqttClient = new MqttClient("tcp://" + hostname + ":" + port, clientId);
        this.objectMapper = objectMapper;
        this.monitorMap = new HashMap<>();


        mqttClient.connect();
    }

    public void init(){
        if(!init) {
            init = true;
            try {
                mqttClient.subscribeWithResponse(baseTopic + "/sqhandler/"+clientId+"/resp", (tpic, msg) -> {
                    resp.set(msg.toString());
                    Payload response = objectMapper.readValue(resp.get(), Payload.class);
                    Payload r = monitorMap.get(response.getUid());
                    if (!r.getFunction().equals(response.getFunction()))
                        return;
                    if (!r.getClientId().equals(response.getClientId()))
                        return;
                    r.setResponse(response.getResponse());
                    synchronized (r) {
                        r.notifyAll();
                    }
                });
            } catch (MqttException e) {
                init = false;
                e.printStackTrace();
            }
        }
    }

    public synchronized  <T> T mqttRPC(String function, Object request, Class<T> tClass) throws Exception
    {



        String uuid = UUID.randomUUID().toString();
        Payload payload = new Payload();
        payload.setUid(uuid);
        payload.setClientId(clientId);
        payload.setFunction(function);
        payload.setRequest(request);

        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(objectMapper.writeValueAsBytes(payload));
        mqttMessage.setQos(2);
        mqttMessage.setRetained(false);

        monitorMap.put(uuid, payload);

        mqttClient.publish(baseTopic + "/sqhandler/req", mqttMessage);

        synchronized (payload) {
            payload.wait();
        }

        if (resp.get().equals("null"))
            return null;

        Payload response = objectMapper.readValue(resp.get(), Payload.class);
        return objectMapper.convertValue(response.getResponse(), tClass);
    }
}
