package com.example.activity_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activity_manager.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
