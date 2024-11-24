package com.github.arifjehoh.taskmanager.tasks;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_seq")
    private Long id;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS", nullable = false)
    private String status;
    @Column(name = "PRIORITY", nullable = false)
    private String priority;
    @Column(name = "DUE_DATE")
    private LocalDateTime dueDate;
    @Column(name = "CREATED_AT")
    private Timestamp createdAt;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "ASSIGNEE_USER_ID")
    private Long assigneeId;

    public Task() {
    }

    public Task(
            Long id,
            String title,
            String description,
            String status,
            String priority,
            LocalDateTime dueDate,
            Long assigneeId,
            String createdBy,
            String updatedBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.assigneeId = assigneeId;
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
        this.createdBy = createdBy;
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now());
        this.updatedBy = updatedBy;
    }

    public Task(TaskForm form, String createdBy, String updatedBy) {
        this(null, form.title(), form.description(), form.status(), form.priority(), form.getDueDate(),null, createdBy, updatedBy);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Task update(TaskForm form, String currentUser, Long assignee) {
        return new Task(this.id,
                Optional.ofNullable(form.title())
                        .orElse(this.title),
                Optional.ofNullable(form.description())
                        .orElse(this.description),
                Optional.ofNullable(form.status())
                        .orElse(this.status),
                Optional.ofNullable(form.priority())
                        .orElse(this.priority),
                Optional.ofNullable(form.getDueDate())
                        .orElse(this.dueDate),
                Optional.ofNullable(assignee)
                        .orElse(this.assigneeId),
                this.createdBy,
                currentUser);
    }
}
