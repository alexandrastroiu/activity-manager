package com.example.activity_manager.dto;

import com.example.activity_manager.model.CourseSession;

import java.time.LocalDate;
import java.time.LocalTime;

public class CourseSessionResponseDto {

    private Long sessionId;
    private Long courseId;
    private String courseName;

    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private LocalTime duration;

    // Constructor
    public CourseSessionResponseDto(
            Long sessionId,
            Long courseId,
            String courseName,
            LocalDate sessionDate,
            LocalTime sessionTime,
            LocalTime duration
    ) {
        this.sessionId = sessionId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.duration = duration;
    }

    // Getters
    public Long getSessionId() {
        return sessionId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
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

    public static CourseSessionResponseDto from(CourseSession s) {
        return new CourseSessionResponseDto(
                s.getSessionId(),
                s.getCourse().getCourseId(),
                s.getCourse().getCourseName(),
                s.getSessionDate(),
                s.getSessionTime(),
                s.getDuration()
        );
    }
}
