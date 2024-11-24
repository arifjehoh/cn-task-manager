package com.github.arifjehoh.taskmanager.tasks;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findAll() {
        return (List<Task>) repository.findAll();
    }

    public Task save(TaskForm form, String createdBy) {
        form.verify();
        Task task = new Task(form, createdBy, null);
        return save(task);
    }

    public Task findById(Long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    public Task update(Long id, TaskForm form) {
        if (id == null) {
            throw new IllegalArgumentException("Task ID is required");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid Task ID");
        }
        form.verify();
        Task task = repository.findById(id)
                              .map(data -> data.update(form))
                              .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return save(task);
    }

    private Task save(Task task) {
        return repository.save(task);
    }
}
