package com.example.activity_manager.service;

import com.example.activity_manager.model.Activity;
import com.example.activity_manager.model.ActivityStatus;
import com.example.activity_manager.model.Priority;
import com.example.activity_manager.model.Difficulty;
import com.example.activity_manager.repository.ActivityRepository;
import com.example.activity_manager.repository.ActivitySubtaskRepository;
import com.example.activity_manager.dto.ActivityCreateDto;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivitySubtaskRepository subtaskRepository;

    public ActivityService(ActivityRepository activityRepository, ActivitySubtaskRepository subtaskRepository) {
        this.activityRepository = activityRepository;
        this.subtaskRepository = subtaskRepository;
    }

    // CRUD operations

    // Create activity
    public Activity create(Activity activity) {
        return activityRepository.save(activity);
    }

    // Delete activity
    public void delete(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new RuntimeException("Activity not found");
        }
        activityRepository.deleteById(id);
    }

    // Update activity
    public Activity update(Long activityId, ActivityCreateDto dto) {
        Activity a = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found"));

        int progress = calculateProgress(activityId);

        // All completed activities must have progress as 100%
        // Prevent user from marking an activity as completed if progress is not 100%
        if (dto.getStatus() == ActivityStatus.COMPLETED && progress < 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot set COMPLETED unless progress is 100%"
            );
        }

        // Update fields
        a.setTitle(dto.getTitle());
        a.setDescription(dto.getDescription());
        a.setStartDate(dto.getStartDate());
        a.setEndDate(dto.getEndDate());
        a.setStatus(dto.getStatus());
        a.setPriority(dto.getPriority());
        a.setDifficulty(dto.getDifficulty());

        return activityRepository.save(a);
    }

    // Find an activity by activity ID
    public Activity getById(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found"));
    }

    // Find all
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    // Get activities for a teacher
    public List<Activity> getActivitiesByTeacher(Long teacherId) {
        return activityRepository.findByTeacher_TeacherId(teacherId);
    }

    // Sort activities by deadline
    public List<Activity> getActivitiesSortedByDeadline(Long teacherId) {
        return activityRepository.findByTeacher_TeacherIdOrderByEndDateAsc(teacherId);
    }

    // Filtering methods for activities

    // Filter activities by status
    public List<Activity> getActivitiesByStatus(Long teacherId, ActivityStatus status) {
        return activityRepository.findByTeacher_TeacherIdAndStatus(teacherId, status);
    }

    // Filter activities by priority
    public List<Activity> getActivitiesByPriority(
            Long teacherId,
            Priority priority
    ) {
        return activityRepository
                .findByTeacher_TeacherIdAndPriority(teacherId, priority);
    }

    // Filter activities by difficulty level
    public List<Activity> getActivitiesByDifficulty(
            Long teacherId,
            Difficulty difficulty
    ) {
        return activityRepository
                .findByTeacher_TeacherIdAndDifficulty(teacherId, difficulty);
    }


    // Calculate progress for an activity
    public int calculateProgress(Long activityId) {
        long total = subtaskRepository.countByActivity_ActivityId(activityId);
        long completed = subtaskRepository
                .countByActivity_ActivityIdAndIsCompletedTrue(activityId);

        return total == 0 ? 0 : (int) ((completed * 100) / total);
    }


    // Count completed activities
    public long countCompletedActivities(Long teacherId) {
        return activityRepository.countByTeacher_TeacherIdAndStatus(
                teacherId, ActivityStatus.COMPLETED
        );
    }

    // Count the total number of activities
    public long countTotalActivities(Long teacherId) {
        return activityRepository.countByTeacher_TeacherId(teacherId);
    }


    // Calculate average progress for all activities
    public double getAverageProgress(Long teacherId) {
        List<Activity> activities = getActivitiesByTeacher(teacherId);

        if (activities.isEmpty()) return 0;

        return activities.stream()
                .mapToInt(a -> calculateProgress(a.getActivityId()))
                .average()
                .orElse(0);
    }

}