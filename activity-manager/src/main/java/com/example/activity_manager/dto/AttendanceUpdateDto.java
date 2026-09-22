package com.example.activity_manager.dto;

import com.example.activity_manager.enums.AttendanceStatus;

import jakarta.validation.constraints.NotNull;

public class AttendanceUpdateDto {
    
    // Validation rules
    @NotNull(message = "Attendance status must not be empty")
    private AttendanceStatus status;

    // Getter
    public AttendanceStatus getStatus() {
        return status;
    }

    // Setter
    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}
