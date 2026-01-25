package com.ajaros.reservationsystem.auth.dtos;

import com.ajaros.reservationsystem.validation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
        @ValidPassword
        String password) {
  public RegisterRequest {
    name = name.trim();
    surname = surname.trim();
  }
}
