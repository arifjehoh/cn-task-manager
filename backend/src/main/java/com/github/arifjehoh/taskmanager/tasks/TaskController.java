package com.github.arifjehoh.taskmanager.tasks;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> getTasks() {
        return service.findAll();
    }

    @PostMapping
    public Task createTask(
            Principal principal,
            @RequestBody TaskForm task) {
        try {
            String currentUser = principal.getName();
            return service.save(task, currentUser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid form entry, due to: %s".formatted(e.getMessage()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create task, exception is not handled");
        }
    }

    @GetMapping("/{id}")
    public Object getTask(@PathVariable(value = "id") Long id) {
        return new Object();
    }

    @PutMapping("/{id}")
    public Object updateTask(@PathVariable(value = "id") Long id) {
        return new Object();
    }

    @PatchMapping("/{id}")
    public Object updateTaskPartially(@PathVariable(value = "id") Long id) {
        return new Object();
    }

    @DeleteMapping("/{id}")
    public Object deleteTask(@PathVariable(value = "id") Long id) {
        return new Object();
    }

}
