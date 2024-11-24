package com.github.arifjehoh.taskmanager.tasks;

public record TaskAssignmentForm(String assignee) {
    public void verify() {
        if (assignee == null || assignee.isBlank()) {
            throw new IllegalArgumentException("Assignee is required");
        }
    }
}
