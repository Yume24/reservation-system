package com.ajaros.reservationsystem.users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserInformationRequest(
    @NotBlank(message = "Name cannot be blank")
        @Size(max = 255, message = "Name must not be longer than 255 characters")
        String name,
    @NotBlank(message = "Surname cannot be blank")
        @Size(max = 255, message = "Surname must not be longer than 255 characters")
        String surname) {}
