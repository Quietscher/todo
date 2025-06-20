package de.wittmannq.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Task {

    private String description;

    private boolean completed;

    private LocalDateTime createdAt;

    @Id
    @GeneratedValue
    private Long id;

    public Task() {
    }

    public Task(String description, boolean completed, LocalDateTime createdAt, Long id) {
        this.description = description;
        this.completed = completed;
        this.createdAt = createdAt;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
