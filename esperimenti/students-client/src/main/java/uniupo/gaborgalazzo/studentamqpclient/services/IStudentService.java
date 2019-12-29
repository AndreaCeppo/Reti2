package uniupo.gaborgalazzo.studentamqpclient.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import uniupo.gaborgalazzo.studentamqpclient.model.Student;

import java.util.List;

public interface IStudentService
{
	public Student addStudent( Student student) throws Exception;

	public List<Student> getAllStudents(String search) throws Exception;

	public Student getStudentById(long id) throws Exception;

	public void deleteStudentById(long id) throws Exception;

	public Student updateStudent(Student student) throws Exception;
}
