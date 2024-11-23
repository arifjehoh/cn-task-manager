package com.github.arifjehoh.taskmanager.tasks;

public class TaskPriority {
    public static final String HIGH = "high";
    public static final String MEDIUM = "medium";
    public static final String LOW = "low";

    public static boolean of(String priority) {
        return switch (priority.toLowerCase()) {
            case HIGH, MEDIUM, LOW -> true;
            default -> false;
        };
    }

    public static String list() {
        return String.join(", ", HIGH, MEDIUM, LOW);
    }
}
