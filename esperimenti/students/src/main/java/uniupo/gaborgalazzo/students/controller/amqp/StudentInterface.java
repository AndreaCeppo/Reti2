package uniupo.gaborgalazzo.students.controller.amqp;

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
import uniupo.gaborgalazzo.students.model.Request;
import uniupo.gaborgalazzo.students.model.Student;
import uniupo.gaborgalazzo.students.service.StudentService;

@Service
@Configuration
public class StudentInterface
{

	private final StudentService studentService;

	@Value("${rabbitmq.basetopic}")
	private String baseTopic;

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

	@Autowired
	public StudentInterface(StudentService studentService)
	{
		this.studentService = studentService;
	}

	@RabbitListener(queues = "${rabbitmq.basetopic}.student.rpc.requests")
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
							studentService.getStudentById((int) request.getData())
					);
				case Request.FUN_SEARCH_ALL:
					return objectMapper.writeValueAsString(
							studentService.getAllStudents((String) request.getData())
					);
				case Request.FUN_ADD:
					return objectMapper.writeValueAsString(
							studentService.addStudent(objectMapper.readValue((String) request.getData(), Student.class))
					);
				case Request.FUN_EDIT:
					return objectMapper.writeValueAsString(
							studentService.updateStudent(objectMapper.readValue((String) request.getData(), Student.class))
					);

				case Request.FUN_DELETE:
					studentService.deleteStudentById((int) request.getData());
					return objectMapper.writeValueAsString(true);
			}
			return null;
		} catch (Exception e)
		{
			return objectMapper.writeValueAsString(e);
		}
	}
}
