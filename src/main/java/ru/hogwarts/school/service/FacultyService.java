package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> findFacultyById(Long id) {
        return facultyRepository.findById(id);
    }

    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFacultyById(Long id) {
        facultyRepository.deleteById(id);
    }

    public List<Faculty> findFacultiesByColor(String color) {
        return facultyRepository.findAllByColorIgnoreCase(color);
    }

    public List<Faculty> findFacultiesByNameOrColor(String name, String color) {
        String searchName = (name != null && !name.trim().isEmpty()) ? name : null;
        String searchColor = (color != null && !color.trim().isEmpty()) ? color : null;

        if (searchName != null && searchColor != null) {
            return facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(searchName, searchColor);
        } else if (searchName != null) {
            return facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(searchName, searchName);
        } else if (searchColor != null) {
            return facultyRepository.findAllByColorIgnoreCase(searchColor);
        }
        return Collections.emptyList();
    }

    public List<Student> findStudentsByFacultyId(Long facultyId) {
        return studentRepository.findByFaculty_Id(facultyId);
    }
}