package com.example.activity_manager.controller;

import com.example.activity_manager.dto.LoginRequestDto;
import com.example.activity_manager.dto.UserResponseDto;
import com.example.activity_manager.model.User;
import com.example.activity_manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Login endpoint for teachers
    // POST
    @PostMapping("/login")
    public UserResponseDto login(@Valid @RequestBody LoginRequestDto dto) {
        User user = userService.authenticateTeacher(dto.getUsername(), dto.getPassword());
        return UserResponseDto.from(user);
    }
}