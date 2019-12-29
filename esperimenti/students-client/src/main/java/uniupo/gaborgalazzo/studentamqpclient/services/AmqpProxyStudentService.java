package uniupo.gaborgalazzo.studentamqpclient.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uniupo.gaborgalazzo.studentamqpclient.model.Request;
import uniupo.gaborgalazzo.studentamqpclient.model.Student;

import java.util.ArrayList;
import java.util.List;

@Service
public class AmqpProxyStudentService implements IStudentService
{




	private final RabbitTemplate template;

	private DirectExchange exchange;

	private final ObjectMapper objectMapper;

	public AmqpProxyStudentService(
			RabbitTemplate template,
			ObjectMapper objectMapper,
			@Value("${rabbitmq.proxy.basetopic}") String baseTopic
	)
	{
		this.exchange = new DirectExchange(baseTopic + ".student.rpc");;
		this.template = template;
		this.objectMapper = objectMapper;
	}

	@Override
	public Student addStudent(Student student) throws Exception
	{

		Request request = new Request();
		request.setFunction(Request.FUN_ADD);
		request.setData(objectMapper.writeValueAsString(student));
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

		Request request = new Request();
		request.setFunction(Request.FUN_SEARCH_ALL);
		request.setData(search);
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
		Request request = new Request();
		request.setFunction(Request.FUN_FIND_BY_ID);
		request.setData(id);
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
		Request request = new Request();
		request.setFunction(Request.FUN_DELETE);
		request.setData(id);
		template.convertSendAndReceive
				(exchange.getName(), "rpc", objectMapper.writeValueAsString(request));
	}

	@Override
	public Student updateStudent(Student student) throws Exception
	{
		Request request = new Request();
		request.setFunction(Request.FUN_EDIT);
		request.setData(objectMapper.writeValueAsString(student));
		String response = (String) template.convertSendAndReceive
				(exchange.getName(), "rpc", objectMapper.writeValueAsString(request));
		if (response != null)
			return objectMapper.readValue(response, Student.class);
		else
			return null;
	}
}
