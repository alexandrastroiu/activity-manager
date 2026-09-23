package com.example.activity_manager.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class AttendanceId implements Serializable {

    private Long sessionId;
    private Long studentId;

    // Default constructor
    public AttendanceId() {
    }

    public AttendanceId(Long sessionId, Long studentId) {
        this.sessionId = sessionId;
        this.studentId = studentId;
    }

    // Equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof AttendanceId)) {
            return false;
        }

        AttendanceId otherId = (AttendanceId) obj;
        return Objects.equals(sessionId, otherId.sessionId) && Objects.equals(studentId, otherId.studentId);
    }

    // Hashcode method
    @Override
    public int hashCode() {
        return Objects.hash(sessionId, studentId);
    }
}
