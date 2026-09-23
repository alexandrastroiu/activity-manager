package com.example.activity_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activity_manager.model.CourseSession;

public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {

    List<CourseSession> findByCourse_CourseId(Long courseId);

    Long countByCourse_CourseId(Long courseId);
}
