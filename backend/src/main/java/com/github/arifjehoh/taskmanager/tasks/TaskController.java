package com.github.arifjehoh.taskmanager.tasks;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            @RequestBody TaskForm form) {
        try {
            String currentUser = principal.getName();
            return service.save(form, currentUser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid form entry, due to: %s".formatted(e.getMessage()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create task, exception is not handled");
        }
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Task updateTask(Principal principal,
                           @PathVariable(value = "id") Long id,
                           @RequestBody TaskForm form) {
        try {
            String currentUser = principal.getName();
            form.verify();
            return service.update(id, form, currentUser);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid form entry, due to: %s".formatted(e.getMessage()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update task, exception is not handled");
        }
    }

    @PatchMapping(value = "/{id}", name = "updateTaskPartially")
    public Task updateTaskPartially(Principal principal,
                                    @PathVariable(value = "id") Long id,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "description", required = false) String description,
                                    @RequestParam(value = "status", required = false) String status,
                                    @RequestParam(value = "priority", required = false) String priority,
                                    @RequestParam(value = "dueDate", required = false) String dueDate) {
        TaskForm form = new TaskForm(title, description, status, priority, dueDate);
        String currentUser = principal.getName();
        return service.patch(id, form, currentUser);
    }

    @PatchMapping(value = "/{id}/assign", name = "assignTask")
    public Task assignTask(@PathVariable(value = "id") Long id,
                           @RequestBody TaskAssignmentForm form) {
        form.verify();
        try {
            return service.assign(id, form);
        } catch (UsernameNotFoundException e) {
            throw new IllegalArgumentException("Assigned user not found");
        }
    }

    @DeleteMapping("/{id}")
    public Task deleteTask(@PathVariable(value = "id") Long id) {
        return service.delete(id);
    }

}
