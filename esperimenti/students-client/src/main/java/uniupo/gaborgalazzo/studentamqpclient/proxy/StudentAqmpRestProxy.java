package uniupo.gaborgalazzo.studentamqpclient.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import uniupo.gaborgalazzo.studentamqpclient.model.Request;
import uniupo.gaborgalazzo.studentamqpclient.model.Student;
import uniupo.gaborgalazzo.studentamqpclient.services.RestStudentService;

@Service
@Configuration
public class StudentAqmpRestProxy
{


	@Value("${rabbitmq.proxy.basetopic}")
	private String baseTopic;

	@Autowired
	private RestStudentService restStudentService;

	@Bean
	public Queue queue() {
		return new Queue(baseTopic + ".student.rpc.requests");
	}

	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(baseTopic + ".student.rpc");
	}

	@Bean
	public Binding binding(DirectExchange exchange,
						   Queue queue) {
		return BindingBuilder.bind(queue)
				.to(exchange)
				.with("rpc");
	}

	@RabbitListener(queues = "${rabbitmq.proxy.basetopic}.student.rpc.requests")
	public String handler(String message) throws JsonProcessingException
	{

		ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			System.out.println("request_function = " + message);
			Request request = objectMapper.readValue(message, Request.class);

			switch (request.getFunction())
			{
				case Request.FUN_FIND_BY_ID:
					return objectMapper.writeValueAsString(
							restStudentService.getStudentById((int) request.getData())
					);
				case Request.FUN_SEARCH_ALL:
					return objectMapper.writeValueAsString(
							restStudentService.getAllStudents((String) request.getData())
					);
				case Request.FUN_ADD:
					return objectMapper.writeValueAsString(
							restStudentService.addStudent(objectMapper.readValue((String) request.getData(), Student.class))
					);
				case Request.FUN_EDIT:
					return objectMapper.writeValueAsString(
							restStudentService.updateStudent(objectMapper.readValue((String) request.getData(), Student.class))
					);

				case Request.FUN_DELETE:
					restStudentService.deleteStudentById((int) request.getData());
					return objectMapper.writeValueAsString(true);
			}
			return null;
		} catch (Exception e)
		{
			return objectMapper.writeValueAsString(e);
		}
	}
}
