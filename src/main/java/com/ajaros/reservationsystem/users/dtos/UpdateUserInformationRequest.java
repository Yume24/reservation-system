package com.ajaros.reservationsystem.users.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserInformationRequest(
    @Schema(example = "John")
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 255, message = "Name must not be longer than 255 characters")
        String name,
    @Schema(example = "Doe")
        @NotBlank(message = "Surname cannot be blank")
        @Size(max = 255, message = "Surname must not be longer than 255 characters")
        String surname) {
  public UpdateUserInformationRequest {
    name = name.trim();
    surname = surname.trim();
  }
}
