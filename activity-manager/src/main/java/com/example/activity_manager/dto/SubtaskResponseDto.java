package com.example.activity_manager.dto;

import com.example.activity_manager.model.ActivitySubtask;

public class SubtaskResponseDto {

    private Long subtaskId;
    private String title;
    private boolean completed;

    public SubtaskResponseDto(
            Long subtaskId,
            String title,
            boolean completed
    ) {
        this.subtaskId = subtaskId;
        this.title = title;
        this.completed = completed;
    }

    public static SubtaskResponseDto from(ActivitySubtask subtask) {
        return new SubtaskResponseDto(
                subtask.getSubtaskId(),
                subtask.getTitle(),
                subtask.getIsCompleted()
        );
    }

    // Getters
    public Long getSubtaskId() {
        return subtaskId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }
}
