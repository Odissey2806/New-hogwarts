package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.ResourceNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addFaculty(@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.createFaculty(faculty);
        return createdFaculty.getId();
    }

    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.findFacultyById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Факультет с id " + id + " не найден"));
    }

    @PutMapping
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.findFacultyById(faculty.getId())
                .map(existingFaculty -> facultyService.updateFaculty(faculty))
                .orElseThrow(() -> new ResourceNotFoundException("Факультет для обновления с id " + faculty.getId() + " не найден"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFaculty(@PathVariable Long id) {
        facultyService.findFacultyById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Факультет для удаления с id " + id + " не найден"));
        facultyService.deleteFacultyById(id);
    }

    @GetMapping("/color/{color}")
    public List<Faculty> getFacultiesByColor(@PathVariable String color) {
        return facultyService.findFacultiesByColor(color);
    }

    @GetMapping("/search")
    public List<Faculty> searchFaculties(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) String color) {
        return facultyService.findFacultiesByNameOrColor(name, color);
    }

    @GetMapping("/{id}/students")
    public List<Student> getStudentsByFaculty(@PathVariable Long id) {
        facultyService.findFacultyById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Факультет с id " + id + " не найден"));
        return facultyService.findStudentsByFacultyId(id);
    }

    @GetMapping("/longest-name")
    public String getFacultyWithLongestName() {
        return facultyService.getFacultyWithLongestName();
    }
}

