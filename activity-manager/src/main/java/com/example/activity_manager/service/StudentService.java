package com.example.activity_manager.service;

import com.example.activity_manager.model.Student;
import com.example.activity_manager.model.CourseEnrollment;
import com.example.activity_manager.repository.StudentRepository;
import com.example.activity_manager.repository.CourseEnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseEnrollmentRepository enrollmentRepository;

    // Constructor
    public StudentService(StudentRepository studentRepository, CourseEnrollmentRepository courseEnrollmentRepository) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = courseEnrollmentRepository;
    }

    // Get students enrolled in a specific courses
    public List<Student> getStudentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseCourseId(courseId)
                .stream()
                .map(CourseEnrollment::getStudent)
                .toList();
    }

}
