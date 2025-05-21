package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.ResourceNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addStudent(@RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return createdStudent.getId();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.findStudentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Студент с id " + id + " не найден"));
    }

    @PutMapping
    public Student updateStudent(@RequestBody Student student) {
        return studentService.findStudentById(student.getId())
        .map(existingStudent -> studentService.updateStudent(student))
        .orElseThrow(() -> new ResourceNotFoundException("Студент для обновления с id " + student.getId() + " не найден"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.findStudentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Студент для удаления с id " + id + " не найден"));
        studentService.deleteStudentById(id);
    }

    @GetMapping("/age/{age}")
    public List<Student> getStudentsByAge(@PathVariable int age) {
        return studentService.findStudentsByAge(age);
    }


    @GetMapping("/age/between")
    public List<Student> getStudentsByAgeRange(@RequestParam int min, @RequestParam int max) {
        return studentService.findStudentsByAgeBetween(min, max);
    }


    @GetMapping("/{id}/faculty")
    public Faculty getFacultyByStudent(@PathVariable Long id) {
        return studentService.findFacultyByStudentId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Факультет для студента с id " + id + " не найден (возможно, студент или факультет не существуют)"));
    }

    @GetMapping("/count")
    public Integer getStudentsCount() {
        return studentService.getTotalCountOfStudents();
    }

    @GetMapping("/avg-age")
    public Double getStudentsAverageAge() {
        return studentService.getAverageAgeOfStudents();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.findLastFiveStudents();
    }
}
