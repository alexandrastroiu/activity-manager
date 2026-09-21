package com.example.activity_manager.controller;

import com.example.activity_manager.dto.CourseResponseDto;
import com.example.activity_manager.dto.TeacherDashboardDto;
import com.example.activity_manager.service.ActivityService;
import com.example.activity_manager.service.CourseService;
import com.example.activity_manager.service.StudentService;
import com.example.activity_manager.service.TeacherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final ActivityService activityService;
    private final CourseService courseService;
    private final StudentService studentService;

    public TeacherController(TeacherService teacherService,
                             ActivityService activityService,
                             CourseService courseService,
                             StudentService studentService) {
        this.teacherService = teacherService;
        this.activityService = activityService;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    // Get dashboard
    // GET
    @GetMapping("/user/{userId}/dashboard")
    public TeacherDashboardDto myDashboard(@PathVariable Long userId) {
        Long teacherId = teacherService.getByUserId(userId).getTeacherId();

        long total = activityService.countTotalActivities(teacherId);
        long completed = activityService.countCompletedActivities(teacherId);
        double avgProgress = activityService.getAverageProgress(teacherId);

        return new TeacherDashboardDto(total, completed, avgProgress);
    }

    // Get courses
    // GET
    @GetMapping("/user/{userId}/courses")
    public List<CourseResponseDto> myCourses(@PathVariable Long userId) {
        Long teacherId = teacherService.getByUserId(userId).getTeacherId();

        return courseService.getCoursesByTeacher(teacherId).stream()
                .map(CourseResponseDto::from)
                .toList();
    }

}