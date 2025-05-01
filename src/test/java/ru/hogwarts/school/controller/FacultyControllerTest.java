package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    private static final String BASE_URL = "http://localhost:";
    private static final String FACULTY_ENDPOINT = "/faculty";
    private static final String TEST_FACULTY_NAME = "Gryffindor";
    private static final String TEST_FACULTY_COLOR = "Red";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getFacultyUrl() {
        return BASE_URL + port + FACULTY_ENDPOINT;
    }

    @Test
    void shouldAddFacultyAndReturnCreatedStatus() {
        Faculty faculty = createTestFaculty();

        ResponseEntity<Long> response = restTemplate.postForEntity(
                getFacultyUrl(),
                faculty,
                Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldGetFacultyByIdAndReturnOkStatus() {
        Faculty faculty = createTestFaculty();
        Long id = restTemplate.postForObject(getFacultyUrl(), faculty, Long.class);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getFacultyUrl() + "/" + id,
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals(TEST_FACULTY_NAME, response.getBody().getName());
    }

    @Test
    void shouldUpdateFacultyAndReturnOkStatus() {
        Faculty faculty = createTestFaculty();
        Long id = restTemplate.postForObject(getFacultyUrl(), faculty, Long.class);
        faculty.setId(id);
        faculty.setName("Updated Faculty");

        ResponseEntity<Faculty> response = restTemplate.exchange(
                getFacultyUrl(),
                HttpMethod.PUT,
                new HttpEntity<>(faculty),
                Faculty.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Faculty", response.getBody().getName());
    }

    @Test
    void shouldDeleteFacultyAndReturnNoContentStatus() {
        Faculty faculty = createTestFaculty();
        Long id = restTemplate.postForObject(getFacultyUrl(), faculty, Long.class);

        restTemplate.delete(getFacultyUrl() + "/" + id);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getFacultyUrl() + "/" + id,
                Faculty.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldGetFacultiesByColorAndReturnOkStatus() {
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                getFacultyUrl() + "/color/" + TEST_FACULTY_COLOR,
                Faculty[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldSearchFacultiesAndReturnOkStatus() {
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                getFacultyUrl() + "/search?name=" + TEST_FACULTY_NAME + "&color=" + TEST_FACULTY_COLOR,
                Faculty[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenFacultyDoesNotExist() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getFacultyUrl() + "/999",
                Faculty.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private Faculty createTestFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName(TEST_FACULTY_NAME);
        faculty.setColor(TEST_FACULTY_COLOR);
        return faculty;
    }
}