package com.example.activity_manager.service;

import com.example.activity_manager.model.Teacher;
import com.example.activity_manager.repository.TeacherRepository;
import com.example.activity_manager.repository.CourseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    public TeacherService(TeacherRepository teacherRepository, CourseRepository courseRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    // Get teacher by user ID
    public Teacher getByUserId(Long userId) {
        return teacherRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
    }

    // Get teacher with department
    public Teacher getTeacherWithDepartment(Long userId) {
        return teacherRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
    }

    // Get the number of courses for a teacher
    public long countCoursesForTeacher(Long teacherId) {
        return courseRepository.countByTeacher_TeacherId(teacherId);
    }

}