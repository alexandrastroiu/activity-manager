package com.example.activity_manager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_groups")
public class StudentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    // Default constructor
    public StudentGroup() {
    }

    // Getters
    public Long getStudentGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    // Setters
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
