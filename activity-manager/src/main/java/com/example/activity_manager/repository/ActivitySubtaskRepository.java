package com.example.activity_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activity_manager.model.ActivitySubtask;

public interface ActivitySubtaskRepository extends JpaRepository<ActivitySubtask, Long> {

    List<ActivitySubtask> findByActivity_ActivityId(Long activityId);

    long countByActivity_ActivityId(Long activityId);

    long countByActivity_ActivityIdAndIsCompletedTrue(Long activityId);
}
