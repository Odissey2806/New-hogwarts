package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);
    List<Student> findAllByAgeBetween(int minAge, int maxAge);
    List<Student> findByFaculty_Id(Long facultyId);

    @Query("SELECT COUNT(s) FROM Student s")
    Integer getTotalCountOfStudents();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double getAverageAgeOfStudents();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();
}