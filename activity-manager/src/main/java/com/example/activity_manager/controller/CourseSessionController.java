package com.example.activity_manager.controller;

import com.example.activity_manager.dto.CreateCourseSessionDto;
import com.example.activity_manager.dto.CourseSessionResponseDto;
import com.example.activity_manager.dto.UpdateCourseSessionDto;
import com.example.activity_manager.model.Course;
import com.example.activity_manager.model.CourseSession;
import com.example.activity_manager.service.CourseService;
import com.example.activity_manager.service.CourseSessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class CourseSessionController {

    private final CourseSessionService sessionService;
    private final CourseService courseService;

    public CourseSessionController(CourseSessionService sessionService, CourseService courseService) {
        this.sessionService = sessionService;
        this.courseService = courseService;
    }

    // Create course session
    // POST
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CourseSessionResponseDto createSession(@Valid @RequestBody CreateCourseSessionDto dto) {
        Course course = courseService.getById(dto.getCourseId());

        CourseSession session = new CourseSession();
        session.setCourse(course);
        session.setSessionDate(dto.getSessionDate());
        session.setSessionTime(dto.getSessionTime());
        session.setDuration(dto.getDuration());

        CourseSession saved = sessionService.createSession(session);
        return CourseSessionResponseDto.from(saved);
    }

    // Get sessions for a course
    // GET
    @GetMapping("/course/{courseId}")
    public List<CourseSessionResponseDto> getSessionsByCourse(@PathVariable Long courseId) {
        return sessionService.getSessionsByCourse(courseId).stream()
                .map(CourseSessionResponseDto::from)
                .toList();
    }

    // Update course session
    // PUT
    @PutMapping("/{sessionId}")
    public CourseSessionResponseDto updateSession(
            @PathVariable Long sessionId,
            @Valid @RequestBody UpdateCourseSessionDto dto
    ) {
        CourseSession updated = new CourseSession();
        updated.setSessionDate(dto.getSessionDate());
        updated.setSessionTime(dto.getSessionTime());
        updated.setDuration(dto.getDuration());

        CourseSession saved = sessionService.updateSession(sessionId, updated);
        return CourseSessionResponseDto.from(saved);
    }

    // Delete course session
    // DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{sessionId}")
    public void deleteSession(@PathVariable Long sessionId) {
        sessionService.deleteSession(sessionId);
    }

}