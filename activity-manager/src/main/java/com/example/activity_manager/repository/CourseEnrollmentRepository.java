package com.example.activity_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activity_manager.model.CourseEnrollment;
import com.example.activity_manager.model.CourseEnrollmentId;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, CourseEnrollmentId> {

    List<CourseEnrollment> findByCourse_CourseId(Long courseId);
}
