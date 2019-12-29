package uniupo.gaborgalazzo.studentamqpclient.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uniupo.gaborgalazzo.studentamqpclient.model.Student;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestStudentService implements IStudentService
{

	private final RestTemplate restTemplate;

	@Value("${api.baseurl}")
	private String baseURL;

	public RestStudentService(RestTemplateBuilder restTemplateBuilder){
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public Student addStudent(Student student)
	{
		return restTemplate.postForObject(baseURL + "/student", student, Student.class);
	}

	@Override
	public List<Student> getAllStudents(String search)
	{
		return restTemplate.getForObject(baseURL + "/student?search=" + search, new ArrayList<Student>().getClass());
	}

	@Override
	public Student getStudentById(long id)
	{
		return restTemplate.getForObject(baseURL + "/student/" + id, Student.class);
	}

	@Override
	public void deleteStudentById(long id)
	{
		restTemplate.delete(baseURL + "/student/" + id);
	}

	@Override
	public Student updateStudent(Student student)
	{
		restTemplate.put(baseURL + "/student", student);
		return student;
	}
}
