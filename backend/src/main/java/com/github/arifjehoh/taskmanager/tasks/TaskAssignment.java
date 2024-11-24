package com.github.arifjehoh.taskmanager.tasks;

import com.github.arifjehoh.taskmanager.auth.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "task_assignment")
public class TaskAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_assignment_id_seq")
    private Long id;
    @Column(name = "task_id")
    private Long taskId;
    @Column(name = "assigned_user_id")
    private Long assignee;

    public TaskAssignment() {
    }

    public TaskAssignment(Long taskId, UserDTO user) {
        this.taskId = taskId;
        this.assignee = user.id();
    }
}
