package com.example.activity_manager.dto;

import com.example.activity_manager.model.UserRole;
import com.example.activity_manager.model.User;

public class UserResponseDto {

    private Long userId;
    private String username;
    private String email;
    private UserRole role;

    // Constructor
    public UserResponseDto(
            Long userId,
            String username,
            String email,
            UserRole role
    ) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Getters
    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public static UserResponseDto from(User u) {
        return new UserResponseDto(
                u.getUserId(),
                u.getUsername(),
                u.getUserEmail(),
                u.getUserRole()
        );
    }
}
