package com.example.activity_manager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "activity_subtasks")
public class ActivitySubtask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtask_id")
    private Long subtaskId;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted = false;

    // Default constructor
    public ActivitySubtask() {
    }

    // Constructor
    public ActivitySubtask(Activity activity, String title) {
        this.activity = activity;
        this.title = title;
        this.isCompleted = false;
    }

    // Constructor
    public ActivitySubtask(Activity activity, String title, boolean isCompleted) {
        this.activity = activity;
        this.title = title;
        this.isCompleted = isCompleted;
    }

    // Getters
    public Long getSubtaskId() {
        return subtaskId;
    }

    public Activity getActivity() {
        return activity;
    }

    public String getTitle() {
        return title;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    // Setters
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
