package com.example.activity_manager.repository;

import com.example.activity_manager.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacher_TeacherId(Long teacherId);

    long countByTeacher_TeacherId(Long teacherId);
}