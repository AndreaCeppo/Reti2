package uniupo.gaborgalazzo.students.controller;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uniupo.gaborgalazzo.students.model.Student;
import uniupo.gaborgalazzo.students.repository.IStudentRepository;
import uniupo.gaborgalazzo.students.util.CustomRsqlVisitor;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class StudentController {

    private final IStudentRepository studentRepository;

    @Autowired
    public StudentController(IStudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @RequestMapping(
            value = "/student",
            method = {RequestMethod.POST}
    )
    public Student add(@RequestBody Student student){
        student.setId(null);
        return studentRepository.save(student);
    }

    @RequestMapping(
            value = "/student",
            method = {RequestMethod.GET},
            produces = {"application/json", "application/xml"}
    )
    public List<Student> getAll(
            @ApiParam(value =
                    "Examples of RSQL expressions in both FIQL-like and alternative notation:\n" +
                    "- name==\"Kill Bill\";year=gt=2003\n" +
                    "- name==\"Kill Bill\" and year>2003\n" +
                    "- genres=in=(sci-fi,action);(director=='Christopher Nolan',actor==*Bale);year=ge=2000\n" +
                    "- genres=in=(sci-fi,action) and (director=='Christopher Nolan' or actor==*Bale) and year>=2000\n" +
                    "- director.lastName==Nolan;year=ge=2000;year=lt=2010\n" +
                    "- director.lastName==Nolan and year>=2000 and year<2010\n" +
                    "- genres=in=(sci-fi,action);genres=out=(romance,animated,horror),director==Que*Tarantino\n" +
                    "- genres=in=(sci-fi,action) and genres=out=(romance,animated,horror) or director==Que*Tarantino")
            @RequestParam(required = false) String search){

        if(search != null) {
            Node rootNode = new RSQLParser().parse(search);
            Specification<Student> spec = rootNode.accept(new CustomRsqlVisitor<Student>());
            return studentRepository.findAll(spec);
        }else {
            return  studentRepository.findAll();
        }

    }

    @RequestMapping(
            value = "/student/{id}",
            method = {RequestMethod.GET},
            produces = {"application/json", "application/xml"}
    )
    public Student getById(@PathVariable("id") long id){
        return studentRepository.findById(id).get();
    }

    @RequestMapping(
            value = "/student/{id}",
            method = {RequestMethod.DELETE},
            produces = {"application/json", "application/xml"}
    )
    public void deleteById(@PathVariable("id") long id){
        studentRepository.deleteById(id);
    }

    @RequestMapping(
            value = "/student",
            method = {RequestMethod.PUT}
    )
    public Student update(@RequestBody Student student){
        return studentRepository.save(student);
    }
}
