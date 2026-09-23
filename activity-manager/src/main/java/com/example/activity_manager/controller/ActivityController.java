package com.example.activity_manager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.activity_manager.dto.ActivityCreateDto;
import com.example.activity_manager.dto.ActivityResponseDto;
import com.example.activity_manager.dto.TeacherDashboardDto;
import com.example.activity_manager.enums.ActivityStatus;
import com.example.activity_manager.enums.Difficulty;
import com.example.activity_manager.enums.Priority;
import com.example.activity_manager.model.Activity;
import com.example.activity_manager.model.Teacher;
import com.example.activity_manager.service.ActivityService;
import com.example.activity_manager.service.TeacherService;
import com.example.activity_manager.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;
    private final TeacherService teacherService;
    private final UserService userService;

    public ActivityController(ActivityService activityService, TeacherService teacherService, UserService userService) {
        this.activityService = activityService;
        this.teacherService = teacherService;
        this.userService = userService;
    }

    // Create activity
    // POST
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ActivityResponseDto createActivity(
            Principal principal,
            @Valid @RequestBody ActivityCreateDto dto
    ) {
        Teacher teacher = getCurrentTeacher(principal);
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
    @GetMapping
    public List<ActivityResponseDto> getActivitiesForTeacher(
            Principal principal
    ) {
        return activityService.getActivitiesByTeacher(getCurrentTeacher(principal).getTeacherId())
                .stream()
                .map(a -> ActivityResponseDto.from(
                a,
                activityService.calculateProgress(a.getActivityId())
        ))
                .toList();
    }

    // Get activities by status
    // GET
    @GetMapping("/status/{status}")
    public List<ActivityResponseDto> getByStatus(
            Principal principal,
            @PathVariable ActivityStatus status
    ) {
        return activityService.getActivitiesByStatus(getCurrentTeacher(principal).getTeacherId(), status)
                .stream()
                .map(a -> ActivityResponseDto.from(
                a,
                activityService.calculateProgress(a.getActivityId())
        ))
                .toList();
    }

    // Get activities by priority
    //GET
    @GetMapping("/priority/{priority}")
    public List<ActivityResponseDto> getByPriority(
            Principal principal,
            @PathVariable Priority priority
    ) {
        return activityService.getActivitiesByPriority(getCurrentTeacher(principal).getTeacherId(), priority)
                .stream()
                .map(a -> ActivityResponseDto.from(
                a,
                activityService.calculateProgress(a.getActivityId())
        ))
                .toList();
    }

    // Get activities by difficulty
    // GET
    @GetMapping("/difficulty/{difficulty}")
    public List<ActivityResponseDto> getByDifficulty(
            Principal principal,
            @PathVariable Difficulty difficulty
    ) {
        return activityService.getActivitiesByDifficulty(getCurrentTeacher(principal).getTeacherId(), difficulty)
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
    @GetMapping("/statistics")
    public TeacherDashboardDto getDashboard(Principal principal) {
        Long teacherId = getCurrentTeacher(principal).getTeacherId();
        long total = activityService.countTotalActivities(teacherId);
        long completed = activityService.countCompletedActivities(teacherId);
        double avgProgress = activityService.getAverageProgress(teacherId);

        return new TeacherDashboardDto(total, completed, avgProgress);
    }

    // Sort activities by deadline
    // GET
    @GetMapping("/sorted/deadline")
    public List<ActivityResponseDto> getSortedByDeadline(Principal principal) {
        Long teacherId = getCurrentTeacher(principal).getTeacherId();
        
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

    // Helper method
    private Teacher getCurrentTeacher(Principal principal) {
        Long userId = userService.findByUsername(principal.getName()).getUserId();
        return teacherService.getByUserId(userId);
    }
}
