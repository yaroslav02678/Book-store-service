package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetDTO {
    private String token;

    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    private String password;

    @NotEmpty(message = "Please confirm your password.")
    private String confirmPassword;
}