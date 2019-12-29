package uniupo.gaborgalazzo.students.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import uniupo.gaborgalazzo.students.model.Student;
public interface IStudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {



}
