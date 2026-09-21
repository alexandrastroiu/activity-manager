package com.example.activity_manager.service;

import com.example.activity_manager.model.CourseSession;
import com.example.activity_manager.repository.CourseSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CourseSessionService {

    private final CourseSessionRepository sessionRepository;

    // Constructor
    public CourseSessionService(CourseSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    // Find sessions related to a specific course
    public List<CourseSession> getSessionsByCourse(Long courseId) {
        return sessionRepository.findByCourse_CourseId(courseId);
    }

    // CRUD operations

    // Create session
    public CourseSession createSession(CourseSession session) {
        return sessionRepository.save(session);
    }

    // Update session
    public CourseSession updateSession(Long sessionId, CourseSession updated) {
        CourseSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        // update session date
        session.setSessionDate(updated.getSessionDate());
        // update session time
        session.setSessionTime(updated.getSessionTime());
        // update session duration
        session.setDuration(updated.getDuration());

        return sessionRepository.save(session);
    }

    // Delete session
    public void deleteSession(Long sessionId) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        sessionRepository.deleteById(sessionId);
    }

    // Find a session by session ID
    public CourseSession getById(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
    }

}