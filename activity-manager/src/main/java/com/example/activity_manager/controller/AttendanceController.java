package com.example.activity_manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.activity_manager.dto.AttendanceCreateDto;
import com.example.activity_manager.dto.AttendanceResponseDto;
import com.example.activity_manager.model.Attendance;
import com.example.activity_manager.service.AttendanceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Create or update attendance record
    // POST
    @PostMapping
    public AttendanceResponseDto markAttendance(@Valid @RequestBody AttendanceCreateDto dto) {
        Attendance saved = attendanceService.markAttendance(dto.getSessionId(), dto.getStudentId(), dto.getStatus());
        return AttendanceResponseDto.from(saved);
    }

    // Get attendance for a specific sesssion
    // GET
    @GetMapping("/session/{sessionId}")
    public List<AttendanceResponseDto> getAttendanceForSession(@PathVariable Long sessionId) {
        return attendanceService.getAttendanceForSession(sessionId).stream()
                .map(AttendanceResponseDto::from)
                .toList();
    }

    // Delete one attendance record
    // DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/session/{sessionId}/student/{studentId}")
    public void deleteAttendance(@PathVariable Long sessionId, @PathVariable Long studentId) {
        attendanceService.deleteAttendance(sessionId, studentId);
    }
}
