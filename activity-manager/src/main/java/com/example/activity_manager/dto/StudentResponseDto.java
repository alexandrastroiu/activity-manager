package com.example.activity_manager.dto;

import com.example.activity_manager.model.Student;

public class StudentResponseDto {
    private Long studentId;
    private String firstName;
    private String lastName;
    private String groupName;

    public StudentResponseDto(Long studentId, String firstName, String lastName, String groupName) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupName = groupName;
    }

    // Getters
    public Long getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGroupName() {
        return groupName;
    }

    public static StudentResponseDto from(Student s) {
        return new StudentResponseDto(
                s.getStudentId(),
                s.getFirstName(),
                s.getLastName(),
                s.getStudentGroup().getGroupName()
        );
    }
}
