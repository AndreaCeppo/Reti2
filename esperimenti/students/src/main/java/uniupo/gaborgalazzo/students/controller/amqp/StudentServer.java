package uniupo.gaborgalazzo.students.controller.amqp;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.remoting.service.AmqpInvokerServiceExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uniupo.gaborgalazzo.students.model.Student;
import uniupo.gaborgalazzo.students.service.StudentService;

import java.util.List;

@Service
public class StudentServer {

    private final StudentService studentService;

    private final AmqpTemplate template;
    private final ConnectionFactory factory;

    @Autowired
    public StudentServer(StudentService studentService, AmqpTemplate template, ConnectionFactory factory) {
        this.studentService = studentService;
        this.template = template;
        this.factory = factory;

        AmqpInvokerServiceExporter exporter = new AmqpInvokerServiceExporter();
        exporter.setService(this);
        exporter.setAmqpTemplate(template);
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factory);
        container.setMessageListener(exporter);
        container.setQueueNames("${rabbitmq.basetopic}.rpc.students.*");
    }



    @RabbitListener(queues = "${rabbitmq.basetopic}.rpc.students.add")
    public Student add(Student student){
        return studentService.addStudent(student);
    }

    @RabbitListener(queues = "${rabbitmq.basetopic}.rpc.students.all")
    public List<Student> getAll(
           String search){

        return studentService.getAllStudents(search);

    }


    @RabbitListener(queues = "${rabbitmq.basetopic}.rpc.students.get")
    public Student getById(long id){
        return studentService.getStudentById(id);
    }


    @RabbitListener(queues = "${rabbitmq.basetopic}.rpc.students.delete")
    public void deleteById(long id){
        studentService.deleteStudentById(id);
    }


    @RabbitListener(queues = "${rabbitmq.basetopic}.rpc.students.update")
    public Student update(Student student){
        return studentService.updateStudent(student);
    }
}
