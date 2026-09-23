package com.example.activity_manager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.activity_manager.model.CourseEnrollment;
import com.example.activity_manager.model.Student;
import com.example.activity_manager.repository.CourseEnrollmentRepository;
import com.example.activity_manager.repository.StudentRepository;

@Service
public class StudentService {

    private final CourseEnrollmentRepository enrollmentRepository;

    // Constructor
    public StudentService(StudentRepository studentRepository, CourseEnrollmentRepository courseEnrollmentRepository) {
        this.enrollmentRepository = courseEnrollmentRepository;
    }

    // Get students enrolled in a specific courses
    public List<Student> getStudentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(CourseEnrollment::getStudent)
                .toList();
    }

}
