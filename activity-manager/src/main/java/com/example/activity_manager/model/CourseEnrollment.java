package com.example.activity_manager.model;

import com.example.activity_manager.enums.EnrollmentType;
import jakarta.persistence.*;

@Entity
@Table(name = "course_enrollment")
public class CourseEnrollment {

    @EmbeddedId
    private CourseEnrollmentId enrollmentId; // composite Primary Key

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_type")
    private EnrollmentType enrollmentType = EnrollmentType.ACTIVE;

    // Default constructor
    public CourseEnrollment() {
    }

    // Constructor
    public CourseEnrollment(Course course, Student student, EnrollmentType enrollmentType) {
        this.student = student;
        this.course = course;
        this.enrollmentId = new CourseEnrollmentId(
                course.getCourseId(),
                student.getStudentId()
        );
        this.enrollmentType = enrollmentType;
    }

    // Getters
    public CourseEnrollmentId getId() {
        return enrollmentId;
    }

    public Course getCourse() {
        return course;
    }

    public Student getStudent() {
        return student;
    }

    public EnrollmentType getEnrollmentType() {
        return enrollmentType;
    }

    // Setters
    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setEnrollmentType(EnrollmentType enrollmentType) {
        this.enrollmentType = enrollmentType;
    }
}
