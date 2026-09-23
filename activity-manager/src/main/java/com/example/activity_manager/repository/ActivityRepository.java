package com.example.activity_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activity_manager.enums.ActivityStatus;
import com.example.activity_manager.enums.Difficulty;
import com.example.activity_manager.enums.Priority;
import com.example.activity_manager.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByTeacher_TeacherId(Long teacherId);

    List<Activity> findByTeacher_TeacherIdAndStatus(
            Long teacherId,
            ActivityStatus status
    );

    List<Activity> findByTeacher_TeacherIdAndPriority(
            Long teacherId,
            Priority priority
    );

    List<Activity> findByTeacher_TeacherIdAndDifficulty(
            Long teacherId,
            Difficulty difficulty
    );

    List<Activity> findByTeacher_TeacherIdOrderByEndDateAsc(Long teacherId);

    long countByTeacher_TeacherIdAndStatus(
            Long teacherId,
            ActivityStatus status
    );

    long countByTeacher_TeacherId(Long teacherId);

}
