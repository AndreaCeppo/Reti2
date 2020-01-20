package uniupo.gaborgalazzo.studentamqpclient.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import uniupo.gaborgalazzo.studentamqpclient.model.Payload;
import uniupo.gaborgalazzo.studentamqpclient.model.Student;
import uniupo.gaborgalazzo.studentamqpclient.services.RestStudentService;

@Configuration
public class StudentMqttSingleQueueRestProxy
{

	@Value("${mqtt.hostname}")
	private String hostname;
	@Value("${mqtt.port}")
	private int port;
	@Value("${mqtt.proxy.basetopic}")
	private String topic;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RestStudentService restStudentService;
	@Bean
	public MessageChannel mqttSqInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundSq() {
		MqttPahoMessageDrivenChannelAdapter adapter =
				new MqttPahoMessageDrivenChannelAdapter("tcp://" + hostname + ":" + port, "server-mqtt-sq",
						topic + "/sqhandler/req");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttSqInputChannel());
		return adapter;
	}

	@Bean
	public IMqttClient mqttSqResponder() throws MqttException {


		return new MqttClient("tcp://" + hostname + ":" + port, "resp-mqtt-sq");
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttSqInputChannel")
	public MessageHandler handlerSq(IMqttClient mqttResponder) {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException
			{

				try
				{
					MqttMessage mqttMessage = new MqttMessage();
					Payload request = objectMapper.readValue(message.getPayload().toString(), Payload.class);

					Object resp = handleRequest(request);

					Payload respPayload = new Payload();
					respPayload.setUid(request.getUid());
					respPayload.setFunction(request.getFunction());
					respPayload.setResponse(resp);
					respPayload.setClientId(request.getClientId());

					mqttMessage.setPayload(objectMapper.writeValueAsBytes(respPayload));
					mqttMessage.setQos(2);
					mqttMessage.setRetained(false);

					String respTopic = topic + "/sqhandler/"+request.getClientId()+"/resp";
					mqttResponder.connect();
					mqttResponder.publish(respTopic, mqttMessage);

					mqttResponder.disconnect();
					mqttResponder.close();

				} catch (Exception e)
				{
					// e.printStackTrace();
				}

			}

			public Object handleRequest(Payload request) throws JsonProcessingException
			{
				ObjectMapper objectMapper = new ObjectMapper();
				try
				{
					switch (request.getFunction())
					{
						case Payload.FUN_FIND_BY_ID:
							return restStudentService.getStudentById((int) request.getRequest());
						case Payload.FUN_SEARCH_ALL:
							return restStudentService.getAllStudents((String) request.getRequest());
						case Payload.FUN_ADD:
							return restStudentService.addStudent(objectMapper.readValue((String) request.getRequest(), Student.class));
						case Payload.FUN_EDIT:
							return restStudentService.updateStudent(objectMapper.readValue((String) request.getRequest(), Student.class));

						case Payload.FUN_DELETE:
							restStudentService.deleteStudentById((int) request.getRequest());
							return true;
					}
					return null;
				} catch (Exception e)
				{
					return e.getMessage();
				}
			}

		};
	}




}
