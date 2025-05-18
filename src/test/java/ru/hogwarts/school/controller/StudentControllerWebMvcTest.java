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
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    private static final String STUDENT_NAME = "Test Student";
    private static final int STUDENT_AGE = 20;
    private static final long STUDENT_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void shouldCreateStudentAndReturnCreatedStatus() throws Exception {
        Student student = createTestStudent();
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + STUDENT_NAME + "\",\"age\":" + STUDENT_AGE + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(STUDENT_ID));
    }

    @Test
    void shouldGetStudentByIdAndReturnOkStatus() throws Exception {
        Student student = createTestStudent();
        when(studentService.findStudentById(anyLong())).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", STUDENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID))
                .andExpect(jsonPath("$.name").value(STUDENT_NAME))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE));
    }

    @Test
    void shouldReturnNotFoundWhenStudentNotExists() throws Exception {
        when(studentService.findStudentById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", STUDENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateStudentAndReturnOkStatus() throws Exception {
        Student updatedStudent = createTestStudent();
        updatedStudent.setName("Updated Student");
        when(studentService.updateStudent(any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":" + STUDENT_ID + ",\"name\":\"Updated Student\",\"age\":" + STUDENT_AGE + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_ID))
                .andExpect(jsonPath("$.name").value("Updated Student"))
                .andExpect(jsonPath("$.age").value(STUDENT_AGE));
    }

    @Test
    void shouldDeleteStudentAndReturnNoContentStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/{id}", STUDENT_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetStudentsByAgeAndReturnOkStatus() throws Exception {
        Student student = createTestStudent();
        when(studentService.findStudentsByAge(STUDENT_AGE)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age/{age}", STUDENT_AGE)
                        .param("age", String.valueOf(STUDENT_AGE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(STUDENT_ID))
                .andExpect(jsonPath("$[0].name").value(STUDENT_NAME))
                .andExpect(jsonPath("$[0].age").value(STUDENT_AGE));
    }

    private Student createTestStudent() {
        Student student = new Student();
        student.setId(STUDENT_ID);
        student.setName(STUDENT_NAME);
        student.setAge(STUDENT_AGE);
        return student;
    }
}