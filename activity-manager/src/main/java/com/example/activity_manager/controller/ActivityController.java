package com.example.activity_manager.controller;

import com.example.activity_manager.dto.*;
import com.example.activity_manager.model.Activity;
import com.example.activity_manager.model.Teacher;
import com.example.activity_manager.enums.ActivityStatus;
import com.example.activity_manager.enums.Priority;
import com.example.activity_manager.enums.Difficulty;
import com.example.activity_manager.service.ActivityService;
import com.example.activity_manager.service.TeacherService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityService activityService;
    private final TeacherService teacherService;

    public ActivityController(ActivityService activityService, TeacherService teacherService) {
        this.activityService = activityService;
        this.teacherService = teacherService;
    }

    // Create activity
    // POST
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user/{userId}")
    public ActivityResponseDto createActivity(
            @PathVariable Long userId,
            @Valid @RequestBody ActivityCreateDto dto
    ) {
        Teacher teacher = teacherService.getByUserId(userId);
        Activity activity = new Activity();
        activity.setTeacher(teacher);
        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setStartDate(dto.getStartDate());
        activity.setEndDate(dto.getEndDate());
        activity.setStatus(dto.getStatus());
        activity.setPriority(dto.getPriority());
        activity.setDifficulty(dto.getDifficulty());

        Activity saved = activityService.create(activity);
        int progress = activityService.calculateProgress(saved.getActivityId());

        return ActivityResponseDto.from(saved, progress);
    }

    // Get activities for a teacher
    // GET
    @GetMapping("/teacher/{teacherId}")
    public List<ActivityResponseDto> getActivitiesForTeacher(
            @PathVariable Long teacherId
    ) {
        return activityService.getActivitiesByTeacher(teacherId)
                .stream()
                .map(a -> ActivityResponseDto.from(
                        a,
                        activityService.calculateProgress(a.getActivityId())
                ))
                .toList();
    }

    // Get activities by status
    // GET
    @GetMapping("/teacher/{teacherId}/status/{status}")
    public List<ActivityResponseDto> getByStatus(
            @PathVariable Long teacherId,
            @PathVariable ActivityStatus status
    ) {
        return activityService.getActivitiesByStatus(teacherId, status)
                .stream()
                .map(a -> ActivityResponseDto.from(
                        a,
                        activityService.calculateProgress(a.getActivityId())
                ))
                .toList();
    }

    // Get activities by priority
    //GET
    @GetMapping("/teacher/{teacherId}/priority/{priority}")
    public List<ActivityResponseDto> getByPriority(
            @PathVariable Long teacherId,
            @PathVariable Priority priority
    ) {
        return activityService.getActivitiesByPriority(teacherId, priority)
                .stream()
                .map(a -> ActivityResponseDto.from(
                        a,
                        activityService.calculateProgress(a.getActivityId())
                ))
                .toList();
    }

    // Get activities by difficulty
    // GET
    @GetMapping("/teacher/{teacherId}/difficulty/{difficulty}")
    public List<ActivityResponseDto> getByDifficulty(
            @PathVariable Long teacherId,
            @PathVariable Difficulty difficulty
    ) {
        return activityService.getActivitiesByDifficulty(teacherId, difficulty)
                .stream()
                .map(a -> ActivityResponseDto.from(
                        a,
                        activityService.calculateProgress(a.getActivityId())
                ))
                .toList();
    }

    // Delete an activity
    // DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{activityId}")
    public void deleteActivity(@PathVariable Long activityId) {
        activityService.delete(activityId);
    }

    // Calculate Progress
    // GET
    @GetMapping("/teacher/{teacherId}/stats")
    public TeacherDashboardDto getDashboard(@PathVariable Long teacherId) {

        long total = activityService.countTotalActivities(teacherId);
        long completed = activityService.countCompletedActivities(teacherId);
        double avgProgress = activityService.getAverageProgress(teacherId);

        return new TeacherDashboardDto(total, completed, avgProgress);
    }

    // Sort activities by deadline
    // GET
    @GetMapping("/teacher/{teacherId}/sorted/deadline")
    public List<ActivityResponseDto> getSortedByDeadline(@PathVariable Long teacherId) {
        return activityService.getActivitiesSortedByDeadline(teacherId).stream()
                .map(a -> ActivityResponseDto.from(
                        a,
                        activityService.calculateProgress(a.getActivityId())
                ))
                .toList();
    }

    // Edit an activity
    // PUT
    @PutMapping("/{activityId}")
    public ActivityResponseDto updateActivity(
            @PathVariable Long activityId,
            @Valid @RequestBody ActivityCreateDto dto
    ) {
        Activity updated = activityService.update(activityId, dto);
        int progress = activityService.calculateProgress(updated.getActivityId());
        return ActivityResponseDto.from(updated, progress);
    }
}