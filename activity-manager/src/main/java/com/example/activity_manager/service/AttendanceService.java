package com.example.activity_manager.service;

import com.example.activity_manager.model.Attendance;
import com.example.activity_manager.model.Student;
import com.example.activity_manager.model.CourseSession;
import com.example.activity_manager.model.AttendanceId;
import com.example.activity_manager.model.AttendanceStatus;
import com.example.activity_manager.repository.AttendanceRepository;
import com.example.activity_manager.repository.StudentRepository;
import com.example.activity_manager.repository.CourseSessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final CourseSessionRepository sessionRepository;
    private final StudentRepository studentRepository;

    // Constructor
    public AttendanceService(
            AttendanceRepository attendanceRepository,
            CourseSessionRepository sessionRepository,
            StudentRepository studentRepository
    ) {
        this.attendanceRepository = attendanceRepository;
        this.sessionRepository = sessionRepository;
        this.studentRepository = studentRepository;
    }

    // Mark attendance (create or update)
    public Attendance markAttendance(Long sessionId, Long studentId, AttendanceStatus status) {

        AttendanceId id = new AttendanceId(sessionId, studentId); // composite key

        Attendance attendance = attendanceRepository.findById(id)
                .orElseGet(() -> {
                    CourseSession session = sessionRepository.findById(sessionId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

                    Student student = studentRepository.findById(studentId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

                    return new Attendance(session, student, status);
                });

        attendance.setAttendanceStatus(status);
        return attendanceRepository.save(attendance);
    }

    // Get attendance for a specific course session
    public List<Attendance> getAttendanceForSession(Long sessionId) {
        return attendanceRepository.findBySession_SessionId(sessionId);
    }

    // Delete a specific attendance entry
    public void deleteAttendance(Long sessionId, Long studentId) {
        attendanceRepository.deleteById(new AttendanceId(sessionId, studentId));
    }

    // Validate attendance entry exists ( prevent duplicate entries)
    public boolean attendanceExists(Long sessionId, Long studentId) {
        return attendanceRepository.existsById(
                new AttendanceId(sessionId, studentId)
        );
    }
}