package ma.emsi.studentmvc.Repository;

import ma.emsi.studentmvc.entites.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Page<Student> findByNomContains(String n, Pageable pageable);
}
