package com.example.activity_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activity_manager.model.Attendance;
import com.example.activity_manager.model.AttendanceId;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {

    List<Attendance> findBySession_SessionId(Long sessionId);
}
