package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Phone must not be blank")
    @Pattern(regexp = "\\+?\\d{7,15}", message = "Phone number should be valid")
    private String phone;

    @NotNull(message = "Birth date must not be null")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
}

