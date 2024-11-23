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
        return repository.save(task);
    }
}
