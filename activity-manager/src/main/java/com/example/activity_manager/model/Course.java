package com.example.activity_manager.model;

import com.example.activity_manager.enums.CourseType;
import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_name", nullable = false, unique = true)
    private String courseName;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_type")
    private CourseType courseType = CourseType.MANDATORY;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    // Default constructor
    public Course() {
    }

    // Getters

    public Long getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    // Setters
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
