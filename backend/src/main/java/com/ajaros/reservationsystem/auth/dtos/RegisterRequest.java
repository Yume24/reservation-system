package com.ajaros.reservationsystem.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record RegisterRequest(
    @Schema(example = "user@example.com")
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must not be longer than 255 characters")
        String email,
    @Schema(example = "John")
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 255, message = "Name must not be longer than 255 characters")
        String name,
    @Schema(example = "Doe")
        @NotBlank(message = "Surname cannot be blank")
        @Size(max = 255, message = "Surname must not be longer than 255 characters")
        String surname,
    @Schema(example = "Password123!")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).+$",
            message = "Password must contain uppercase, lowercase, number, and special character")
        String password) {
  public RegisterRequest {
    name = name.trim();
    surname = surname.trim();
  }
}
