package uniupo.gaborgalazzo.students.controller.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import uniupo.gaborgalazzo.students.model.Student;
import uniupo.gaborgalazzo.students.service.StudentService;

import java.util.List;

@Service
@Configuration
public class StudentInterface
{

	private final StudentService studentService;

	private final AmqpTemplate template;
	private final ConnectionFactory factory;

	@Value("${rabbitmq.basetopic}")
	private String baseTopic;

	@Bean
	public Queue queueAdd()
	{
		return new Queue(baseTopic + ".rpc.students.add");
	}

	@Bean
	public Queue queueAll()
	{
		return new Queue(baseTopic + ".rpc.students.all");
	}

	@Bean
	public DirectExchange exchange()
	{
		return new DirectExchange("tut.rpc");
	}

	@Bean
	public Binding bindingAll(DirectExchange exchange,
							  Queue queueAdd)
	{
		return BindingBuilder.bind(queueAdd)
				.to(exchange)
				.with("rpc");
	}


	@Autowired
	public StudentInterface(StudentService studentService, AmqpTemplate template, ConnectionFactory factory)
	{
		this.studentService = studentService;
		this.template = template;
		this.factory = factory;

        /*
        Queue queueAdd = new Queue(baseTopic + ".rpc.students.add");
        Queue queueAll = new Queue(baseTopic + ".rpc.students.all");
		DirectExchange exchange = new DirectExchange(baseTopic + ".rpc.students");
		BindingBuilder.bind(queueAdd)
				.to(exchange)
				.with("rpc");

		BindingBuilder.bind(queueAll)
				.to(exchange)
				.with("rpc");

         */

		/*
        AmqpInvokerServiceExporter exporter = new AmqpInvokerServiceExporter();
        exporter.setService(this);
        exporter.setAmqpTemplate(template);
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factory);
        container.setMessageListener(exporter);
        container.setQueueNames(queueAdd.getName(), queueAll.getName());
		 */
	}


	@RabbitListener(queues = "${rabbitmq.basetopic}.rpc.students.add")
	public Student add(Student student)
	{
		return studentService.addStudent(student);
	}

	@RabbitListener(queues = "${rabbitmq.basetopic}.rpc.students.all")
	public List<Student> getAll(
			String search)
	{

		return studentService.getAllStudents(search);

	}


	//@RabbitListener(queues = "${rabbitmq.basetopic}.rpc.students.get")
	public Student getById(long id)
	{
		return studentService.getStudentById(id);
	}


	//@RabbitListener(queues = "${rabbitmq.basetopic}.rpc.students.delete")
	public void deleteById(long id)
	{
		studentService.deleteStudentById(id);
	}


	//@RabbitListener(queues = "${rabbitmq.basetopic}.rpc.students.update")
	public Student update(Student student)
	{
		return studentService.updateStudent(student);
	}
}
