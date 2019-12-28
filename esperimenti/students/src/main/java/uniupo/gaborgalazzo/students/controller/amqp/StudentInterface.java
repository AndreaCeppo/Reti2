package uniupo.gaborgalazzo.students.controller.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import uniupo.gaborgalazzo.students.model.Student;
import uniupo.gaborgalazzo.students.service.StudentService;

import java.util.List;

@Service
@Configuration
@Profile({"amqp-server"})
public class StudentInterface
{

	private final StudentService studentService;

	@Value("${rabbitmq.basetopic}")
	private String baseTopic;



	@Autowired
	public StudentInterface(StudentService studentService)
	{
		this.studentService = studentService;
		BindingBuilder.bind(new Queue(baseTopic + ".student.rpc.requests"))
				.to(new DirectExchange(baseTopic + ".student.rpc"))
				.with("rpc");
	}

	@RabbitListener(queues = "${rabbitmq.basetopic}.student.rpc.requests")
	public String fibonacci(String request) {
		return request;
	}
}
