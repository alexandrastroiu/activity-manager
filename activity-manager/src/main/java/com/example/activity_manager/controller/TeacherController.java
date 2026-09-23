package com.example.activity_manager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.activity_manager.dto.CourseResponseDto;
import com.example.activity_manager.dto.TeacherDashboardDto;
import com.example.activity_manager.service.ActivityService;
import com.example.activity_manager.service.CourseService;
import com.example.activity_manager.service.StudentService;
import com.example.activity_manager.service.TeacherService;
import com.example.activity_manager.service.UserService;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final ActivityService activityService;
    private final CourseService courseService;
    private final StudentService studentService;
    private final UserService userService;

    public TeacherController(TeacherService teacherService,
            ActivityService activityService,
            CourseService courseService,
            StudentService studentService,
            UserService userService) {
        this.teacherService = teacherService;
        this.activityService = activityService;
        this.courseService = courseService;
        this.studentService = studentService;
        this.userService = userService;
    }

    // Get dashboard
    // GET
    @GetMapping("/dashboard")
    public TeacherDashboardDto myDashboard(Principal principal) {
        Long teacherId = getCurrentTeacherId(principal);

        long total = activityService.countTotalActivities(teacherId);
        long completed = activityService.countCompletedActivities(teacherId);
        double avgProgress = activityService.getAverageProgress(teacherId);

        return new TeacherDashboardDto(total, completed, avgProgress);
    }

    // Get courses
    // GET
    @GetMapping("/courses")
    public List<CourseResponseDto> myCourses(Principal principal) {
        Long teacherId = getCurrentTeacherId(principal);

        return courseService.getCoursesByTeacher(teacherId).stream()
                .map(CourseResponseDto::from)
                .toList();
    }

    // Helper method
    private Long getCurrentTeacherId(Principal principal) {
        Long userId = userService.findByUsername(principal.getName()).getUserId();
        return teacherService.getByUserId(userId).getTeacherId();
    }

}
