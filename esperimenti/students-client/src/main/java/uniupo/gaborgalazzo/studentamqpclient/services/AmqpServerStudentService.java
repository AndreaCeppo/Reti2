package uniupo.gaborgalazzo.studentamqpclient.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uniupo.gaborgalazzo.studentamqpclient.model.Payload;
import uniupo.gaborgalazzo.studentamqpclient.model.Student;

import java.util.ArrayList;
import java.util.List;

@Service
public class AmqpServerStudentService implements IStudentService
{




	private final RabbitTemplate template;

	private DirectExchange exchange;

	private final ObjectMapper objectMapper;

	public AmqpServerStudentService(
			RabbitTemplate template,
			ObjectMapper objectMapper,
			@Value("${rabbitmq.server.basetopic}") String baseTopic
	)
	{
		this.exchange = new DirectExchange(baseTopic + ".student.rpc");;
		this.template = template;
		this.objectMapper = objectMapper;
	}

	@Override
	public Student addStudent(Student student) throws Exception
	{

		Payload request = new Payload();
		request.setFunction(Payload.FUN_ADD);
		request.setRequest(objectMapper.writeValueAsString(student));
		String response = (String) template.convertSendAndReceive
				(exchange.getName(), "rpc", objectMapper.writeValueAsString(request));
		if (response != null)
			return objectMapper.readValue(response, Student.class);
		else
			return null;
	}

	@Override
	public List<Student> getAllStudents(String search) throws Exception
	{

		Payload request = new Payload();
		request.setFunction(Payload.FUN_SEARCH_ALL);
		request.setRequest(search);
		String response = (String) template.convertSendAndReceive
				(exchange.getName(), "rpc", objectMapper.writeValueAsString(request));
		if (response != null)
			return objectMapper.readValue(response, new ArrayList<Student>().getClass());
		else
			return null;
	}

	@Override
	public Student getStudentById(long id) throws Exception
	{
		Payload request = new Payload();
		request.setFunction(Payload.FUN_FIND_BY_ID);
		request.setRequest(id);
		String response = (String) template.convertSendAndReceive
				(exchange.getName(), "rpc", objectMapper.writeValueAsString(request));
		if (response != null)
			return objectMapper.readValue(response, Student.class);
		else
			return null;
	}

	@Override
	public void deleteStudentById(long id) throws Exception
	{
		Payload request = new Payload();
		request.setFunction(Payload.FUN_DELETE);
		request.setRequest(id);
		template.convertSendAndReceive
				(exchange.getName(), "rpc", objectMapper.writeValueAsString(request));
	}

	@Override
	public Student updateStudent(Student student) throws Exception
	{
		Payload request = new Payload();
		request.setFunction(Payload.FUN_EDIT);
		request.setRequest(objectMapper.writeValueAsString(student));
		String response = (String) template.convertSendAndReceive
				(exchange.getName(), "rpc", objectMapper.writeValueAsString(request));
		if (response != null)
			return objectMapper.readValue(response, Student.class);
		else
			return null;
	}
}
