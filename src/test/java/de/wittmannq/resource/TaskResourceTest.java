package de.wittmannq.resource;

import de.wittmannq.entities.Task;
import de.wittmannq.repositories.TaskRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class TaskResourceTest {

    public static final String TEST_TEXT = "test";

    @Inject
    TaskRepository taskRepository;

    @Inject
    TaskResource taskResource;

    @BeforeEach
    @Transactional
    void setUp() {
        Task t = new Task();
        t.setDescription(TEST_TEXT);
        t.setCompleted(false);
        taskRepository.persist(t);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        taskRepository.deleteAll();
    }

    @Test
    void getTasks() {
        given()
                .when()
                .get("/tasks")
                .then()
                .statusCode(200)
                .and()
                .body("[0].description", equalTo(TEST_TEXT));
    }

    @Test
    void createTask() {
        String desc = "wäsche machen";
        given()
                .when()
                .queryParam("description", desc)
                .post("/tasks")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .and()
                .body("description", equalTo(desc));
    }

    @Test
    void updateTask() {
        List<Task> tasks = taskResource.getTasks();
        Long id = tasks.getFirst().getId();
        given()
                .when()
                .contentType("application/json")
                .body("{\"description\":\"dies das\",\"completed\":true,\"id\":" + id + "}")
                .put("/tasks")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    void deleteTask() {
        List<Task> tasks = taskResource.getTasks();
        Long id = tasks.getFirst().getId();
        try (Response response = taskResource.deleteTask(id)) {
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(0, taskRepository.count());
        }
    }
}