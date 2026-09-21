package com.example.activity_manager.dto;

import com.example.activity_manager.model.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public class AttendanceCreateDto {

    // Validation rules
    @NotNull(message = "Session must not be empty")
    private Long sessionId;

    @NotNull(message = "Student must not be empty")
    private Long studentId;

    @NotNull(message = "Status must not be empty")
    private AttendanceStatus status;

    // Getters & Setters
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}
