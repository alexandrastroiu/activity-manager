package com.example.activity_manager.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "course_sessions")
public class CourseSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @Column(name = "session_time", nullable = false)
    private LocalTime sessionTime;

    @Column(name = "duration")
    private LocalTime duration;

    // Default constructor
    public CourseSession() {
    }

    // Getters
    public Long getSessionId() {
        return sessionId;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public LocalTime getSessionTime() {
        return sessionTime;
    }

    public LocalTime getDuration() {
        return duration;
    }

    // Setters
    public void setCourse(Course course) {
        this.course = course;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public void setSessionTime(LocalTime sessionTime) {
        this.sessionTime = sessionTime;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }
}
