package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
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
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.findFacultyById(id)
                .orElse(null);

        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFacultyById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/color/{color}")
    public List<Faculty> getFacultiesByColor(@PathVariable String color) {
        return facultyService.findFacultiesByColor(color);
    }

    @GetMapping("/search")
    public List<Faculty> searchFaculties(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) String color) {
        if (name != null && color != null) {
            return facultyService.findFacultiesByNameOrColor(name, color);
        }
        if (name != null) {
            return facultyService.findFacultiesByNameOrColor(name, "");
        }
        if (color != null) {
            return facultyService.findFacultiesByColor(color);
        }
        return List.of();
    }
}