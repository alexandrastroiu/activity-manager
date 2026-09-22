package com.example.activity_manager.service;

import com.example.activity_manager.model.ActivitySubtask;
import com.example.activity_manager.model.Activity;
import com.example.activity_manager.enums.ActivityStatus;
import com.example.activity_manager.repository.ActivitySubtaskRepository;
import com.example.activity_manager.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ActivitySubtaskService {

    private final ActivitySubtaskRepository subtaskRepository;
    private final ActivityRepository activityRepository;

    public ActivitySubtaskService(ActivitySubtaskRepository subtaskRepository, ActivityRepository activityRepository) {
        this.subtaskRepository = subtaskRepository;
        this.activityRepository = activityRepository;
    }

    // Create subtask
    public ActivitySubtask create(ActivitySubtask subtask) {
        return subtaskRepository.save(subtask);
    }

    // Find all subtasks
    public List<ActivitySubtask> findAll() {
        return subtaskRepository.findAll();
    }

    // Mark subtask
    public ActivitySubtask setCompleted(Long subtaskId, boolean completed) {
        ActivitySubtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subtask not found"));

        subtask.setIsCompleted(completed);
        ActivitySubtask saved = subtaskRepository.save(subtask);

        // If progress is 100%, auto complete the subtask
        // If activity is completed but progress is less than 100%, edit the subtask as in progress

        Long activityId = saved.getActivity().getActivityId();

        long total = subtaskRepository.countByActivity_ActivityId(activityId);
        long done = subtaskRepository.countByActivity_ActivityIdAndIsCompletedTrue(activityId);
        int progress = (total == 0) ? 0 : (int) ((done * 100) / total);

        Activity activity = saved.getActivity();
        if (progress == 100 && activity.getStatus() != ActivityStatus.COMPLETED) {
            activity.setStatus(ActivityStatus.COMPLETED);
            activityRepository.save(activity);
        } else if (progress < 100 && activity.getStatus() == ActivityStatus.COMPLETED) {
            activity.setStatus(ActivityStatus.IN_PROGRESS);
            activityRepository.save(activity);
        }

        return saved;
    }

    // Delete subtask
    public void delete(Long subtaskId) {
        if (!subtaskRepository.existsById(subtaskId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subtask not found");
        }
        subtaskRepository.deleteById(subtaskId);
    }

    // Get subtasks for an activity
    public List<ActivitySubtask> getSubtasksByActivity(Long activityId) {
        return subtaskRepository.findByActivity_ActivityId(activityId);
    }

    // Update the title of a subtask
    public ActivitySubtask updateTitle(Long subtaskId, String title) {
        ActivitySubtask st = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subtask not found"));

        if (title == null || title.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title must not be empty");
        }

        st.setTitle(title.trim());
        return subtaskRepository.save(st);
    }

}