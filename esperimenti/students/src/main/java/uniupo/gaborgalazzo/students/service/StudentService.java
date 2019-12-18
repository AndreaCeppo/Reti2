package uniupo.gaborgalazzo.students.service;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import uniupo.gaborgalazzo.students.model.Student;
import uniupo.gaborgalazzo.students.repository.IStudentRepository;
import uniupo.gaborgalazzo.students.util.CustomRsqlVisitor;

import java.util.List;

@Service
public class StudentService {

    private final IStudentRepository studentRepository;

    @Autowired
    public StudentService(IStudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }



    public Student addStudent( Student student){
        student.setId(null);
        return studentRepository.save(student);
    }


    public List<Student> getAllStudents(String search){
        if(search != null) {
            Node rootNode = new RSQLParser().parse(search);
            Specification<Student> spec = rootNode.accept(new CustomRsqlVisitor<Student>());
            return studentRepository.findAll(spec);
        }else {
            return  studentRepository.findAll();
        }

    }


    public Student getStudentById(long id){
        return studentRepository.findById(id).orElse(null);
    }


    public void deleteStudentById(long id){
        studentRepository.deleteById(id);
    }

    public Student updateStudent(Student student){
        return studentRepository.save(student);
    }
}
