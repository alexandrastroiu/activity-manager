package com.example.activity_manager.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public class UpdateCourseSessionDto {

    // validation rules
    @NotNull(message = "Session date must not be empty")
    private LocalDate sessionDate;

    @NotNull(message = "Session time must not be empty")
    private LocalTime sessionTime;

    @NotNull(message = "Duration must not be empty")
    private LocalTime duration;

    // Getters
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
