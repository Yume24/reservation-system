package com.ajaros.reservationsystem.auth.dtos;

import jakarta.validation.constraints.*;

public record RegisterRequest(
    @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be valid")
        @Size(max = 50, message = "Email must not be longer than 50 characters")
        String email,
    @NotBlank(message = "Name must not be blank")
        @Size(max = 50, message = "Name must not be longer than 50 characters")
        String name,
    @NotBlank(message = "Surname must not be blank")
        @Size(max = 50, message = "Surname must not be longer than 50 characters")
        String surname,
    @NotBlank(message = "Password must not be blank")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%\\^&\\*]).+$",
            message = "Password must contain uppercase, lowercase, number, and special character")
        String password) {}
