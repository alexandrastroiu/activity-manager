package com.example.activity_manager.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateCourseSessionDto {

    // Validation rules
    @NotNull(message = "Course must not be empty")
    @Positive(message = "Course ID must be positive")
    private Long courseId;

    @NotNull(message = "Session date must not be empty")
    @FutureOrPresent(message = "Session date must be today or in the future")
    private LocalDate sessionDate;

    @NotNull(message = "Session time must not be empty")
    private LocalTime sessionTime;

    @NotNull(message = "Duration must not be empty")
    private LocalTime duration;

    // Getters
    public Long getCourseId() {
        return courseId;
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
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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
