package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findAllByColorIgnoreCase(String color);
    List<Faculty> findAllByNameIgnoreCaseOrColorIgnoreCase(String name, String color);
    Optional<Faculty> findByNameIgnoreCase(String name);
}