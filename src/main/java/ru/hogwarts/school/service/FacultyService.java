package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import java.util.HashMap;
import java.util.Map;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long lastId = 0;

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(lastId, faculty);
        return faculty;
    }

    public Faculty findFaculty(long id) {
        return faculties.get(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }

    public Map<Long, Faculty> getAllFaculties() {
        return faculties;
    }

    // Фильтрация по цвету
    public Map<Long, Faculty> filterByColor(String color) {
        Map<Long, Faculty> filtered = new HashMap<>();
        for (Map.Entry<Long, Faculty> entry : faculties.entrySet()) {
            if (entry.getValue().getColor().equalsIgnoreCase(color)) {
                filtered.put(entry.getKey(), entry.getValue());
            }
        }
        return filtered;
    }
}