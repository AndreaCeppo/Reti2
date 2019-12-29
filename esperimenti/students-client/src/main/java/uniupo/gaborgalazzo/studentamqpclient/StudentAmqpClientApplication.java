package uniupo.gaborgalazzo.studentamqpclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class StudentAmqpClientApplication
{

	public static void main(String[] args)
	{
		SpringApplication springApplication =
				new SpringApplicationBuilder()
						.sources(StudentAmqpClientApplication.class)
						.web(WebApplicationType.NONE)
						.build();

		springApplication.run(args);
	}
}
