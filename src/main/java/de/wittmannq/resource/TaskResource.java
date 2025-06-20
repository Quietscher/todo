package de.wittmannq.resource;

import de.wittmannq.entities.Task;
import de.wittmannq.repositories.TaskRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/tasks")
public class TaskResource {
    private static final Logger LOG = Logger.getLogger(TaskResource.class.getName());

    @Inject
    TaskRepository taskRepository;

    @GET
    public List<Task> getMyTasks() {
        return taskRepository.listAll();
    }

    @POST
    @Transactional
    public Task createTask(@QueryParam("description") String description) {
        Task task = new Task();
        task.setDescription(description);
        task.setCreatedAt(LocalDateTime.now());
        task.setCompleted(false);
        taskRepository.persist(task);
        return task;
    }

    @PUT
    @Transactional
    public Response updateTask(Task updated) {
        Optional<Task> taskOptional = taskRepository.findByIdOptional(updated.getId());
        if (taskOptional.isEmpty()) {
            LOG.severe("Task with id " + updated.getId() + " not found");
            return Response.status(NOT_FOUND).build();
        }
        Task task = taskOptional.get();
        task.setDescription(updated.getDescription());
        task.setCompleted(updated.isCompleted());
        return Response.ok().build();
    }

    @DELETE
    @Transactional
    public Response deleteTask(@QueryParam("id") long id) {
        taskRepository.deleteById(id);
        return Response.ok().build();
    }
}