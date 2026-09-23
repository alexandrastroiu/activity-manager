package com.example.activity_manager.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.activity_manager.enums.ActivityStatus;
import com.example.activity_manager.enums.Difficulty;
import com.example.activity_manager.enums.Priority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long activityId;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActivityStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivitySubtask> subtasks = new ArrayList<>();

    ;


    // Default constructor
    public Activity() {
    }

    // Constructor
    public Activity(Teacher teacher, String title) {
        this.teacher = teacher;
        this.title = title;
        this.description = "";
        this.startDate = null;
        this.endDate = LocalDate.now();
        this.status = ActivityStatus.IN_PROGRESS;
        this.priority = Priority.LOW;
        this.difficulty = Difficulty.EASY;
    }

    // Constructor
    public Activity(Teacher teacher, String title, String description, LocalDate startDate, LocalDate endDate, ActivityStatus status, Priority priority, Difficulty difficulty) {
        this.teacher = teacher;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.priority = priority;
        this.difficulty = difficulty;
    }

    // Getters
    public Long getActivityId() {
        return activityId;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Priority getPriority() {
        return priority;
    }

    public List<ActivitySubtask> getSubtasks() {
        return subtasks;
    }

    // Setters
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
