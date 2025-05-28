package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Был вызван метод для создания факультета");
        Faculty createdFaculty = facultyRepository.save(faculty);
        logger.debug("Создан факультет с id = {}", createdFaculty.getId());
        return createdFaculty;
    }

    public Optional<Faculty> findFacultyById(Long id) {
        logger.info("Был вызван метод для поиска факультета по id = {}", id);
        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isEmpty()) {
            logger.warn("Факультет с id = {} не найден", id);
        }
        return faculty;
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Был вызван метод для обновления факультета с id = {}", faculty.getId());
        Faculty updatedFaculty = facultyRepository.save(faculty);
        logger.debug("Обновлен факультет с id = {}", updatedFaculty.getId());
        return updatedFaculty;
    }

    public void deleteFacultyById(Long id) {
        logger.info("Был вызван метод для удаления факультета по id = {}", id);
        facultyRepository.deleteById(id);
        logger.debug("Факультет с id = {} удален", id);
    }

    public List<Faculty> findFacultiesByColor(String color) {
        logger.info("Был вызван метод для поиска факультетов по цвету = {}", color);
        if (color == null || color.isBlank()) {
            logger.warn("Параметр цвета пустой");
            return Collections.emptyList();
        }
        return facultyRepository.findAllByColorIgnoreCase(color);
    }

    public List<Faculty> findFacultiesByNameOrColor(String name, String color) {
        logger.info("Был вызван метод для поиска факультетов по имени = {} или цвету = {}", name, color);

        String searchName = (name != null && !name.trim().isEmpty()) ? name : null;
        String searchColor = (color != null && !color.trim().isEmpty()) ? color : null;

        if (searchName != null && searchColor != null) {
            return facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(searchName, searchColor);
        } else if (searchName != null) {
            return facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(searchName, searchName);
        } else if (searchColor != null) {
            return facultyRepository.findAllByColorIgnoreCase(searchColor);
        }

        logger.warn("Оба параметра (имя и цвет) пустые");
        return Collections.emptyList();
    }

    public List<Student> findStudentsByFacultyId(Long facultyId) {
        logger.info("Был вызван метод для поиска студентов по id факультета = {}", facultyId);
        List<Student> students = studentRepository.findByFaculty_Id(facultyId);
        logger.debug("Найдено {} студентов для факультета с id = {}", students.size(), facultyId);
        return students;
    }
}