package com.ajaros.reservationsystem.rooms.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record RoomDto(
    @Schema(example = "Conference Room A") @NotBlank(message = "Name cannot be blank") String name,
    @Schema(example = "10") @Positive(message = "Capacity must be greater than zero")
        int capacity) {}
