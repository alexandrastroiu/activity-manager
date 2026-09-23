package com.example.activity_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activity_manager.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
