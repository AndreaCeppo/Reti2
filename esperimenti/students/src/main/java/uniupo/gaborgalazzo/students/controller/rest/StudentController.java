package uniupo.gaborgalazzo.students.controller.rest;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import uniupo.gaborgalazzo.students.model.Student;
import uniupo.gaborgalazzo.students.service.StudentService;
import uniupo.gaborgalazzo.students.util.CustomRsqlVisitor;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@Profile({"rest-server"})
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @RequestMapping(
            value = "/student",
            method = {RequestMethod.POST}
    )
    public Student add(@RequestBody Student student){
        return studentService.addStudent(student);
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

       return studentService.getAllStudents(search);

    }

    @RequestMapping(
            value = "/student/{id}",
            method = {RequestMethod.GET},
            produces = {"application/json", "application/xml"}
    )
    public Student getById(@PathParam("id") long id){
        return studentService.getStudentById(id);
    }

    @RequestMapping(
            value = "/student/{id}",
            method = {RequestMethod.DELETE},
            produces = {"application/json", "application/xml"}
    )
    public void deleteById(@PathParam("id") long id){
        studentService.deleteStudentById(id);
    }

    @RequestMapping(
            value = "/student",
            method = {RequestMethod.PUT}
    )
    public Student update(@RequestBody Student student){
        return studentService.updateStudent(student);
    }
}
