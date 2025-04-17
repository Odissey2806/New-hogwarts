package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private long lastId = 0;

    public Student addStudent(Student student) {
        student.setId(++lastId);
        students.put(lastId, student);
        return student;
    }

    public Student findStudent(long id) {
        return students.get(id);
    }

    public Student editStudent(Student student) {
        students.put(student.getId(), student);
        return student;
    }

    public Student deleteStudent(long id) {
        return students.remove(id);
    }

    public Map<Long, Student> getAllStudents() {
        return students;
    }

    // Фильтрация по возрасту
    public Map<Long, Student> filterByAge(int age) {
        Map<Long, Student> filtered = new HashMap<>();
        for (Map.Entry<Long, Student> entry : students.entrySet()) {
            if (entry.getValue().getAge() == age) {
                filtered.put(entry.getKey(), entry.getValue());
            }
        }
        return filtered;
    }
}
