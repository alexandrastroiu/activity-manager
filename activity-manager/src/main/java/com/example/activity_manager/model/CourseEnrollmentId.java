package com.example.activity_manager.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class CourseEnrollmentId implements Serializable {

    private Long studentId;
    private Long courseId;

    // Default constructor
    public CourseEnrollmentId() {
    }

    // Constructor
    public CourseEnrollmentId(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    // Equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof CourseEnrollmentId)) {
            return false;
        }

        CourseEnrollmentId otherId = (CourseEnrollmentId) obj;
        return Objects.equals(studentId, otherId.studentId) && Objects.equals(courseId, otherId.courseId);
    }

    // Hashcode method
    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId);
    }
}
