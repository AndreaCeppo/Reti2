package uniupo.gaborgalazzo.studentamqpclient.amqpclients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import uniupo.gaborgalazzo.studentamqpclient.model.Request;
import uniupo.gaborgalazzo.studentamqpclient.model.Student;
import uniupo.gaborgalazzo.studentamqpclient.services.IStudentService;
import uniupo.gaborgalazzo.studentamqpclient.services.RestStudentService;
import uniupo.gaborgalazzo.studentamqpclient.services.StudentServiceFactory;

import java.util.List;
import java.util.Scanner;

@Configuration
@Component
public class StudentClient implements CommandLineRunner
{


	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private StudentServiceFactory studentServiceFactory;

	private IStudentService studentService;


	private Scanner scanner = new Scanner(System.in);

	@Override
	public void run(String... args) throws Exception
	{
		System.out.println("Select interface: ");
		System.out.println("1. REST");
		System.out.println("2. AMQP SERVER");
		System.out.println("3. AMQP PROXY");
		System.out.println("4. MQTT PROXY");
		int option = scanner.nextInt();
		System.out.println("oprion: "+option);
		switch (option){
			case 1:
				studentService = studentServiceFactory.getRestStudentService();
				break;
			case 2:
				studentService = studentServiceFactory.getAmqpServerStudentService();
				break;
			case 3:
				studentService = studentServiceFactory.getAmqpProxyStudentService();
				break;
			case 4:
				studentService = studentServiceFactory.getMqttProxyStudentService();
				break;
			default:
				System.out.println("Error");
				System.exit(1);
				return;
		}

		while (true)
		{
			switch (selectAction())
			{
				case 1:
					doFindById();
					break;
				case 2:
					doSearchAll();
					break;
				case 3:
					doDeleteById();
					break;
				case 4:
					doAdd();
					break;
				case 5:
					doEdit();
					break;
				case 6:
					System.out.println("Bye");
					System.exit(0);
					return;
			}
		}
	}

	private void doAdd()
	{
		Student student = new Student();
		scanner.nextLine();
		System.out.print("Name: ");
		student.setName(scanner.nextLine());
		System.out.print("Surname: ");
		student.setSurname(scanner.nextLine());
		try
		{
			Student response = studentService.addStudent(student);
			System.out.println(" [.] Got '" + objectMapper.writeValueAsString(response) + "'");
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void doEdit()
	{
		Student student = new Student();
		System.out.println("Id: ");
		student.setId(scanner.nextLong());
		scanner.nextLine();
		System.out.print("Name: ");
		student.setName(scanner.nextLine());
		System.out.print("Surname: ");
		student.setSurname(scanner.nextLine());
		try
		{
			Student response = studentService.updateStudent(student);
			System.out.println(" [.] Got '" + objectMapper.writeValueAsString(response) + "'");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void doDeleteById()
	{
		System.out.println("Id :");
		long id =  scanner.nextLong();
		Request request = new Request();
		request.setFunction(Request.FUN_DELETE);
		request.setData(id);
		try
		{
			studentService.deleteStudentById(id);
			System.out.println(" [.] Got '" + objectMapper.writeValueAsString(true) + "'");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void doSearchAll()
	{
		scanner.nextLine();
		System.out.println("Query :");
		String query =  scanner.nextLine();
		Request request = new Request();
		request.setFunction(Request.FUN_SEARCH_ALL);
		request.setData(query);
		try
		{
			List<Student> response = studentService.getAllStudents(query);
			System.out.println(" [.] Got '" + objectMapper.writeValueAsString(response) + "'");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void doFindById()
	{
		System.out.println("Id :");
		int id =  scanner.nextInt();
		Request request = new Request();
		request.setFunction(Request.FUN_FIND_BY_ID);
		request.setData(id);
		try
		{
			Student response = studentService.getStudentById(id);
			System.out.println(" [.] Got '" + objectMapper.writeValueAsString(response) + "'");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private int selectAction()
	{
		System.out.println("Select Ation:");
		System.out.println("1. FindById");
		System.out.println("2. SearchAll");
		System.out.println("3. DeleteById");
		System.out.println("4. Add");
		System.out.println("5. Edit");
		System.out.println("6. Quit");
		return scanner.nextInt();
	}
}
