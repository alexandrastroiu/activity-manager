package com.example.activity_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    // Login
    // GET
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
