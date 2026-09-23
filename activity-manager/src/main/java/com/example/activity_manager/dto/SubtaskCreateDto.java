package com.example.activity_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubtaskCreateDto {

    // Validation rules
    @NotBlank(message = "Subtask title must not be empty")
    @Size(min = 2, max = 50)
    private String title;

    // Default constructor
    public SubtaskCreateDto() {
    }

    // Setter
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter
    public String getTitle() {
        return title;
    }

}
