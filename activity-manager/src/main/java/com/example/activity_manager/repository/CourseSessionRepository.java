package com.example.activity_manager.repository;

import com.example.activity_manager.model.CourseSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {
    List<CourseSession> findByCourse_CourseId(Long courseId);

    Long countByCourseCourseId(Long courseId);
}