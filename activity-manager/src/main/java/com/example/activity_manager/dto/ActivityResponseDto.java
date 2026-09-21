package com.example.activity_manager.dto;

import com.example.activity_manager.model.Activity;
import com.example.activity_manager.model.ActivityStatus;
import com.example.activity_manager.model.Priority;
import com.example.activity_manager.model.Difficulty;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityResponseDto {

    private Long activityId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ActivityStatus status;
    private Priority priority;
    private Difficulty difficulty;
    private int progress; // calculated
    private List<SubtaskResponseDto> subtasks;

    // Constructor
    public ActivityResponseDto(
            Long activityId,
            String title,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            ActivityStatus status,
            Priority priority,
            Difficulty difficulty,
            int progress,
            List<SubtaskResponseDto> subtasks
    ) {
        this.activityId = activityId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.priority = priority;
        this.difficulty = difficulty;
        this.progress = progress;
        this.subtasks = subtasks;
    }

    public static ActivityResponseDto from(
            Activity activity,
            int progress
    ) {
        return new ActivityResponseDto(
                activity.getActivityId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getStartDate(),
                activity.getEndDate(),
                activity.getStatus(),
                activity.getPriority(),
                activity.getDifficulty(),
                progress,
                activity.getSubtasks()
                        .stream()
                        .map(SubtaskResponseDto::from)
                        .toList()
        );
    }

    // Getters
    public Long getActivityId() {
        return activityId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getProgress() {
        return progress;
    }

    public List<SubtaskResponseDto> getSubtasks() {
        return subtasks;
    }
}
