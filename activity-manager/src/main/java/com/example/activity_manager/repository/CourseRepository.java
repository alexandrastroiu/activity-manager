package com.example.activity_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activity_manager.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTeacher_TeacherId(Long teacherId);

    long countByTeacher_TeacherId(Long teacherId);
}
