package com.example.activity_manager.model;

import com.example.activity_manager.enums.AttendanceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "attendance")
public class Attendance {

    @EmbeddedId
    private AttendanceId attendanceId; // composite Primary Key

    @ManyToOne
    @MapsId("sessionId")
    @JoinColumn(name = "session_id")
    private CourseSession session;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false)
    private AttendanceStatus attendanceStatus = AttendanceStatus.ABSENT;

    // Default constructor
    public Attendance() {
    }

    // Constructor
    public Attendance(CourseSession session, Student student, AttendanceStatus attendanceStatus) {
        this.session = session;
        this.student = student;
        this.attendanceId = new AttendanceId(
                session.getSessionId(),
                student.getStudentId()
        );
        this.attendanceStatus = attendanceStatus;
    }

    // Getters
    public AttendanceId getId() {
        return attendanceId;
    }

    public CourseSession getSession() {
        return session;
    }

    public Student getStudent() {
        return student;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    // Setters
    public void setSession(CourseSession session) {
        this.session = session;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
