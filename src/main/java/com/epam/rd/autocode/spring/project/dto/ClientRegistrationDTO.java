package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClientRegistrationDTO {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Name must not be blank")
    private String name;
}