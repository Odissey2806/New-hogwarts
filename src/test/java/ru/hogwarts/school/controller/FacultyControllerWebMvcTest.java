package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    private static final String FACULTY_NAME = "Gryffindor";
    private static final String FACULTY_COLOR = "Red";
    private static final long FACULTY_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    void shouldCreateFacultyAndReturnCreatedStatus() throws Exception {
        Faculty faculty = createTestFaculty();
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + FACULTY_NAME + "\",\"color\":\"" + FACULTY_COLOR + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(FACULTY_ID));
    }

    @Test
    void shouldGetFacultyByIdAndReturnOkStatus() throws Exception {
        Faculty faculty = createTestFaculty();
        when(facultyService.findFacultyById(anyLong())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", FACULTY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_ID))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
    }

    @Test
    void shouldReturnNotFoundWhenFacultyNotExists() throws Exception {
        when(facultyService.findFacultyById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", FACULTY_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateFacultyAndReturnOkStatus() throws Exception {
        Faculty updatedFaculty = createTestFaculty();
        updatedFaculty.setName("Updated Faculty");
        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":" + FACULTY_ID + ",\"name\":\"Updated Faculty\",\"color\":\"" + FACULTY_COLOR + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_ID))
                .andExpect(jsonPath("$.name").value("Updated Faculty"))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
    }

    @Test
    void shouldDeleteFacultyAndReturnNoContentStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", FACULTY_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetFacultiesByColorAndReturnOkStatus() throws Exception {
        Faculty faculty = createTestFaculty();
        when(facultyService.findFacultiesByColor(FACULTY_COLOR)).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color/{color}", FACULTY_COLOR)
                        .param("color", FACULTY_COLOR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(FACULTY_ID))
                .andExpect(jsonPath("$[0].name").value(FACULTY_NAME))
                .andExpect(jsonPath("$[0].color").value(FACULTY_COLOR));
    }

    @Test
    void shouldSearchFacultiesAndReturnOkStatus() throws Exception {
        Faculty faculty = createTestFaculty();
        when(facultyService.findFacultiesByNameOrColor(FACULTY_NAME, FACULTY_COLOR))
                .thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/search")
                        .param("name", FACULTY_NAME)
                        .param("color", FACULTY_COLOR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(FACULTY_ID))
                .andExpect(jsonPath("$[0].name").value(FACULTY_NAME))
                .andExpect(jsonPath("$[0].color").value(FACULTY_COLOR));
    }

    @Test
    void shouldGetStudentsByFacultyAndReturnOkStatus() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(17);

        when(facultyService.findStudentsByFacultyId(FACULTY_ID)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}/students", FACULTY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[0].age").value(17));
    }

    private Faculty createTestFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(FACULTY_ID);
        faculty.setName(FACULTY_NAME);
        faculty.setColor(FACULTY_COLOR);
        return faculty;
    }
}