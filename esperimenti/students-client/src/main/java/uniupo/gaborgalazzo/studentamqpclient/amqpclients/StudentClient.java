package uniupo.gaborgalazzo.studentamqpclient.amqpclients;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Profile({"amqp-client"})
@Configuration
@Component
public class StudentClient implements CommandLineRunner
{

	@Value("${rabbitmq.basetopic}")
	private String baseTopic;

	@Bean
	public DirectExchange exchange()
	{
		return new DirectExchange(baseTopic + ".student.rpc");
	}

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private DirectExchange exchange;

	private Scanner scanner = new Scanner(System.in);

	@Override
	public void run(String... args) throws Exception
	{


		while (true)
		{
			switch (selectAction())
			{
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					System.out.println("Bye");
					return;
			}
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
