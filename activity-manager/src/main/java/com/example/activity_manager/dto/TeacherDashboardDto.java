package com.example.activity_manager.dto;

public class TeacherDashboardDto {

    private long totalActivities;
    private long completedActivities;
    private double averageProgress;

    public TeacherDashboardDto(
            long totalActivities,
            long completedActivities,
            double averageProgress
    ) {
        this.totalActivities = totalActivities;
        this.completedActivities = completedActivities;
        this.averageProgress = averageProgress;
    }

    // Getters
    public long getTotalActivities() {
        return totalActivities;
    }

    public long getCompletedActivities() {
        return completedActivities;
    }

    public double getAverageProgress() {
        return averageProgress;
    }
}
