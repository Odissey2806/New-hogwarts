package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.hogwarts.school.model.Student;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    private static final String BASE_URL = "http://localhost:";
    private static final String STUDENT_ENDPOINT = "/student";
    private static final String TEST_STUDENT_NAME = "Test Student";
    private static final int TEST_STUDENT_AGE = 20;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getStudentUrl() {
        return BASE_URL + port + STUDENT_ENDPOINT;
    }

    @Test
    void shouldAddStudentAndReturnCreatedStatus() {
        Student student = createTestStudent();

        ResponseEntity<Long> response = restTemplate.postForEntity(
                getStudentUrl(),
                student,
                Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldGetStudentByIdAndReturnOkStatus() {
        Student student = createTestStudent();
        Long id = restTemplate.postForObject(getStudentUrl(), student, Long.class);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                getStudentUrl() + "/" + id,
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals(TEST_STUDENT_NAME, response.getBody().getName());
    }

    @Test
    void shouldUpdateStudentAndReturnOkStatus() {
        Student student = createTestStudent();
        Long id = restTemplate.postForObject(getStudentUrl(), student, Long.class);
        student.setId(id);
        student.setName("Updated Student");

        ResponseEntity<Student> response = restTemplate.exchange(
                getStudentUrl(),
                HttpMethod.PUT,
                new HttpEntity<>(student),
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Student", response.getBody().getName());
    }

    @Test
    void shouldDeleteStudentAndReturnNoContentStatus() {
        Student student = createTestStudent();
        Long id = restTemplate.postForObject(getStudentUrl(), student, Long.class);

        restTemplate.delete(getStudentUrl() + "/" + id);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                getStudentUrl() + "/" + id,
                Student.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldGetStudentsByAgeAndReturnOkStatus() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                getStudentUrl() + "/age/" + TEST_STUDENT_AGE,
                Student[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenStudentDoesNotExist() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                getStudentUrl() + "/999",
                Student.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private Student createTestStudent() {
        Student student = new Student();
        student.setName(TEST_STUDENT_NAME);
        student.setAge(TEST_STUDENT_AGE);
        return student;
    }
}
