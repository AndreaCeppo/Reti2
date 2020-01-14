package uniupo.gaborgalazzo.studentamqpclient.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uniupo.gaborgalazzo.studentamqpclient.model.Payload;
import uniupo.gaborgalazzo.studentamqpclient.model.Student;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class MqttProxyStudentService implements IStudentService
{


	@Value("${mqtt.proxy.basetopic}")
	private String baseTopic;


	private final ObjectMapper objectMapper;
	private final IMqttClient mqttClient;


	public MqttProxyStudentService(
			ObjectMapper objectMapper,
			@Value("${mqtt.hostname}") String hostname,
			@Value("${mqtt.port}") int port
	) throws MqttException
	{
		this.mqttClient = new MqttClient("tcp://" + hostname + ":" + port, "client-mqtt");

		this.objectMapper = objectMapper;
	}


	private synchronized  <T> T mqttRPC(Payload request, Class<T> tClass) throws Exception
	{
		MqttMessage mqttMessage = new MqttMessage();
		mqttMessage.setPayload(objectMapper.writeValueAsBytes(request));
		mqttMessage.setQos(2);
		mqttMessage.setRetained(false);

		String uuid = getMd5(UUID.randomUUID().toString());

		AtomicReference<String> resp = new AtomicReference<>();
		Object monitor = this;


		mqttClient.connect();
		mqttClient.subscribeWithResponse(baseTopic + "/handler/" + uuid + "/resp", (tpic, msg) -> {
			resp.set(msg.toString());
			synchronized (monitor)
			{
				monitor.notifyAll();
			}
		});

		mqttClient.publish(baseTopic + "/handler/" + uuid + "/req", mqttMessage);
		while (resp.get() == null)
		{
			this.wait();
		}
		mqttClient.unsubscribe(baseTopic + "/handler/" + uuid + "/resp");
		mqttClient.disconnect();
		if (resp.get().equals("null"))
			return null;
		return objectMapper.readValue(resp.get(), tClass);
	}

	@Override
	public synchronized List<Student> getAllStudents(String search) throws Exception
	{
		Payload request = new Payload();
		request.setFunction(Payload.FUN_SEARCH_ALL);
		request.setRequest(search);
		return mqttRPC(request, new ArrayList<Student>().getClass());
	}

	private static String getMd5(String input)
	{
		try
		{

			// Static getInstance method is called with hashing MD5


			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			//  of an input digest() return array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32)
			{
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public Student addStudent(Student student) throws Exception
	{

		Payload request = new Payload();
		request.setFunction(Payload.FUN_ADD);
		request.setRequest(objectMapper.writeValueAsString(student));
		return mqttRPC(request, Student.class);
	}


	@Override
	public Student getStudentById(long id) throws Exception
	{
		Payload request = new Payload();
		request.setFunction(Payload.FUN_FIND_BY_ID);
		request.setRequest(id);
		return mqttRPC(request, Student.class);
	}

	@Override
	public void deleteStudentById(long id) throws Exception
	{
		Payload request = new Payload();
		request.setFunction(Payload.FUN_DELETE);
		request.setRequest(id);
		mqttRPC(request, String.class);
	}

	@Override
	public Student updateStudent(Student student) throws Exception
	{
		Payload request = new Payload();
		request.setFunction(Payload.FUN_EDIT);
		request.setRequest(objectMapper.writeValueAsString(student));
		return mqttRPC(request, Student.class);
	}
}
