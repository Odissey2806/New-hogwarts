package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Был вызван метод для создания студента");
        Student createdStudent = studentRepository.save(student);
        logger.debug("Создан студент с id = {}", createdStudent.getId());
        return createdStudent;
    }

    public Optional<Student> findStudentById(Long id) {
        logger.info("Был вызван метод для поиска студента по id = {}", id);
        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) {
            logger.warn("Студент с id = {} не найден", id);
        }
        return student;
    }

    public Student updateStudent(Student student) {
        logger.info("Был вызван метод для обновления студента с id = {}", student.getId());
        Student updatedStudent = studentRepository.save(student);
        logger.debug("Обновлен студент с id = {}", updatedStudent.getId());
        return updatedStudent;
    }

    public void deleteStudentById(Long id) {
        logger.info("Был вызван метод для удаления студента по id = {}", id);
        studentRepository.deleteById(id);
        logger.debug("Студент с id = {} удален", id);
    }

    public List<Student> findStudentsByAge(int age) {
        logger.info("Был вызван метод для поиска студентов по возрасту = {}", age);
        if (age <= 0) {
            logger.warn("Некорректный параметр возраста: {}", age);
        }
        return studentRepository.findAllByAge(age);
    }

    public List<Student> findStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Был вызван метод для поиска студентов в возрасте от {} до {}", minAge, maxAge);
        if (minAge < 0 || maxAge < 0 || minAge > maxAge) {
            logger.warn("Некорректный диапазон возрастов: minAge = {}, maxAge = {}", minAge, maxAge);
        }
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    public Optional<Faculty> findFacultyByStudentId(Long studentId) {
        logger.info("Был вызван метод для поиска факультета по id студента = {}", studentId);
        Optional<Faculty> faculty = studentRepository.findById(studentId)
                .map(Student::getFaculty);
        if (faculty.isEmpty()) {
            logger.warn("Факультет для студента с id = {} не найден", studentId);
        }
        return faculty;
    }

    public Integer getTotalCountOfStudents() {
        logger.info("Был вызван метод для получения общего количества студентов");
        Integer count = studentRepository.getTotalCountOfStudents();
        logger.debug("Общее количество студентов = {}", count);
        return count;
    }

    public Double getAverageAgeOfStudents() {
        logger.info("Был вызван метод для получения среднего возраста студентов");
        Double avgAge = studentRepository.getAverageAgeOfStudents();
        logger.debug("Средний возраст студентов = {}", avgAge);
        return avgAge;
    }

    public List<Student> findLastFiveStudents() {
        logger.info("Был вызван метод для поиска последних пяти студентов");
        List<Student> students = studentRepository.findLastFiveStudents();
        logger.debug("Найдено {} последних студентов", students.size());
        return students;
    }

        public List<String> getStudentNamesStartingWithA() {
            return studentRepository.findAll().stream()
                    .map(Student::getName)
                    .filter(name -> name.toUpperCase().startsWith("A")) // Я чуть лучше фильтр поставил
                    .map(String::toUpperCase)
                    .sorted()
                    .toList();
        }

        public Double getAverageAge() {
            return studentRepository.findAll().stream()
                    .mapToInt(Student::getAge)
                    .average()
                    .orElse(0.0);
    }
}