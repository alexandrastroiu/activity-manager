package com.example.activity_manager.repository;

import com.example.activity_manager.model.ActivitySubtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivitySubtaskRepository extends JpaRepository<ActivitySubtask, Long> {
    List<ActivitySubtask> findByActivity_ActivityId(Long activityId);

    long countByActivity_ActivityId(Long activityId);

    long countByActivity_ActivityIdAndIsCompletedTrue(Long activityId);
}