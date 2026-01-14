package com.ajaros.reservationsystem.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @Schema(example = "user@example.com")
        @Email(message = "Email must be valid")
        @NotBlank(message = "Email cannot be blank")
        String email,
    @Schema(example = "password123") @NotBlank(message = "Password cannot be blank")
        String password) {}
