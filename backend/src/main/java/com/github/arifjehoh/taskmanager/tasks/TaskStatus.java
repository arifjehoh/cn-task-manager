package com.github.arifjehoh.taskmanager.tasks;

public class TaskStatus {
    public static final String IN_PROGRESS = "in progress";
    public static final String TO_DO = "to do";
    public static final String COMPLETED = "completed";

    public static boolean of(String status) {
        return switch (status.toLowerCase()) {
            case IN_PROGRESS, TO_DO, COMPLETED -> true;
            default -> false;
        };
    }

    public static String list() {
        return String.join(", ", IN_PROGRESS, TO_DO, COMPLETED);
    }

}
