package com.example.activity_manager.dto;

import com.example.activity_manager.model.Attendance;
import com.example.activity_manager.model.AttendanceStatus;

public class AttendanceResponseDto {

    private Long sessionId;
    private Long studentId;
    private String studentName;
    private String groupName;
    private AttendanceStatus status;

    // Getters
    public Long getSessionId() {
        return sessionId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getGroupName() {
        return groupName;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    // Constructor
    public AttendanceResponseDto(
            Long sessionId,
            Long studentId,
            String studentName,
            String groupName,
            AttendanceStatus status
    ) {
        this.sessionId = sessionId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.groupName = groupName;
        this.status = status;
    }

    public static AttendanceResponseDto from(Attendance a) {
        return new AttendanceResponseDto(
                a.getSession().getSessionId(),
                a.getStudent().getStudentId(),
                a.getStudent().getFirstName() + " " + a.getStudent().getLastName(),
                a.getStudent().getStudentGroup().getGroupName(),
                a.getAttendanceStatus()
        );
    }
}
