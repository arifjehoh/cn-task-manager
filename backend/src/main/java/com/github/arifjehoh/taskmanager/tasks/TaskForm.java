package com.github.arifjehoh.taskmanager.tasks;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Objects;

public record TaskForm(String title,
                       String description,
                       String status,
                       String priority,
                       String dueDate) {

    public void verify() {
        if (title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        if (status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be blank");
        }
        if (!TaskStatus.of(status)) {
            throw new IllegalArgumentException("Invalid status, must be " + TaskStatus.list());
        }
        if (priority.isBlank()) {
            throw new IllegalArgumentException("Priority cannot be blank");
        }
        if (!TaskPriority.of(priority)) {
            throw new IllegalArgumentException("Invalid priority, must be " + TaskPriority.list());
        }
        if (dueDate != null) {
            if (dueDate.isBlank()) {
                throw new IllegalArgumentException("Due date cannot be blank");
            }
            if (Objects.requireNonNull(getDueDate())
                       .isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Due date cannot be in the past");
            }
        }
    }

    public LocalDateTime getDueDate() {
        if (dueDate == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(dueDate.replace(" ", "T"));
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Invalid due date format, must be yyyy-MM-dd HH:mm. Extra info: " + e.getMessage());
        }
    }

}
