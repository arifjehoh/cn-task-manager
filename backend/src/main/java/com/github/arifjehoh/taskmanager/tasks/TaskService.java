package com.github.arifjehoh.taskmanager.tasks;

import com.github.arifjehoh.taskmanager.auth.AuthService;
import com.github.arifjehoh.taskmanager.auth.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final AuthService authService;
    private final TaskRepository taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;

    public TaskService(AuthService authService, TaskRepository taskRepository, TaskAssignmentRepository taskAssignmentRepository) {
        this.authService = authService;
        this.taskRepository = taskRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
    }

    public List<Task> findAll() {
        return (List<Task>) taskRepository.findAll();
    }

    public Task save(TaskForm form, String createdBy) {
        form.verify();
        Task task = new Task(form, createdBy, null);
        return save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                             .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    public Task update(Long id, TaskForm form, String currentUser) {
        if (id == null) {
            throw new IllegalArgumentException("Task ID is required");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid Task ID");
        }
        form.verify();
        Task task = taskRepository.findById(id)
                                  .map(data -> data.update(form, currentUser))
                                  .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return save(task);
    }

    private Task save(Task task) {
        return taskRepository.save(task);
    }

    public Task patch(Long id, TaskForm form, String currentUser) {
        if (id == null) {
            throw new IllegalArgumentException("Task ID is required");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid Task ID");
        }
        Task task = taskRepository.findById(id)
                                  .map(data -> data.update(form, currentUser))
                                  .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return save(task);
    }

    public Task delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Task ID is required");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid Task ID");
        }
        Task task = taskRepository.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        taskRepository.delete(task);
        return task;
    }

    public Task assign(Long id, TaskAssignmentForm form) {
        if (id == null) {
            throw new IllegalArgumentException("Task ID is required");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid Task ID");
        }
        UserDTO assignee = authService.findByUsername(form.assignee());
        boolean exists = taskRepository.existsById(id);
        if (!exists) {
            throw new IllegalArgumentException("Task not found");
        }
        TaskAssignment taskAssignment = new TaskAssignment(id, assignee);
        taskAssignmentRepository.save(taskAssignment);
        return findById(id);
    }
}
