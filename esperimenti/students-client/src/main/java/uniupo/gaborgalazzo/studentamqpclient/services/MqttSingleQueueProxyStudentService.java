package uniupo.gaborgalazzo.studentamqpclient.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MqttSingleQueueProxyStudentService implements IStudentService
{

	private final ObjectMapper objectMapper;
	private final MqttSingleQueue mqttSingleQueue;

	public MqttSingleQueueProxyStudentService(
			ObjectMapper objectMapper,
			MqttSingleQueue mqttSingleQueue)
	{
		this.objectMapper = objectMapper;
		this.mqttSingleQueue = mqttSingleQueue;
		mqttSingleQueue.init();
	}




	@Override
	public synchronized List<Student> getAllStudents(String search) throws Exception
	{
		return mqttSingleQueue.mqttRPC(Payload.FUN_SEARCH_ALL, search, new ArrayList<Student>().getClass());
	}


	@Override
	public Student addStudent(Student student) throws Exception
	{
		return mqttSingleQueue.mqttRPC(Payload.FUN_ADD,objectMapper.writeValueAsString(student), Student.class);
	}


	@Override
	public Student getStudentById(long id) throws Exception
	{
		return mqttSingleQueue.mqttRPC(Payload.FUN_FIND_BY_ID,id, Student.class);
	}

	@Override
	public void deleteStudentById(long id) throws Exception
	{
		mqttSingleQueue.mqttRPC(Payload.FUN_DELETE,id, Student.class);
	}

	@Override
	public Student updateStudent(Student student) throws Exception
	{
		return mqttSingleQueue.mqttRPC(Payload.FUN_EDIT,objectMapper.writeValueAsString(student), Student.class);
	}
}
