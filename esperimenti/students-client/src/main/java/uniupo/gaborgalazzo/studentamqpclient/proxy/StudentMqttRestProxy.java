package uniupo.gaborgalazzo.studentamqpclient.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
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
public class StudentMqttRestProxy
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
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter =
				new MqttPahoMessageDrivenChannelAdapter("tcp://" + hostname + ":" + port, "server-mqtt",
						topic + "/handler/+/req");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	@Bean
	public IMqttClient mqttResponder() throws MqttException {


		return new MqttClient("tcp://" + hostname + ":" + port, "resp-mqtt");
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler(IMqttClient mqttResponder) {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException
			{

				try
				{
					MqttMessage mqttMessage = new MqttMessage();
					byte[] resp = handleRequest(message.getPayload().toString());
					mqttMessage.setPayload(resp);
					mqttMessage.setQos(2);
					mqttMessage.setRetained(false);
					mqttResponder.connect();
					String respTopic = message.getHeaders().get("mqtt_receivedTopic").toString().replace("/req", "/resp");
					mqttResponder.publish(respTopic, mqttMessage);
					mqttResponder.disconnect();
				} catch (Exception e)
				{
					e.printStackTrace();
				}

			}

			public byte[] handleRequest(String message) throws JsonProcessingException
			{
				ObjectMapper objectMapper = new ObjectMapper();
				try
				{
					Payload request = objectMapper.readValue(message, Payload.class);

					switch (request.getFunction())
					{
						case Payload.FUN_FIND_BY_ID:
							return objectMapper.writeValueAsBytes(
									restStudentService.getStudentById((int) request.getRequest())
							);
						case Payload.FUN_SEARCH_ALL:
							return objectMapper.writeValueAsBytes(
									restStudentService.getAllStudents((String) request.getRequest())
							);
						case Payload.FUN_ADD:
							return objectMapper.writeValueAsBytes(
									restStudentService.addStudent(objectMapper.readValue((String) request.getRequest(), Student.class))
							);
						case Payload.FUN_EDIT:
							return objectMapper.writeValueAsBytes(
									restStudentService.updateStudent(objectMapper.readValue((String) request.getRequest(), Student.class))
							);

						case Payload.FUN_DELETE:
							restStudentService.deleteStudentById((int) request.getRequest());
							return objectMapper.writeValueAsBytes(true);
					}
					return null;
				} catch (Exception e)
				{
					return objectMapper.writeValueAsBytes(e.toString());
				}
			}

		};
	}




}
