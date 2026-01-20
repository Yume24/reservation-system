package com.ajaros.reservationsystem.rooms.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RoomRequest(
    @Schema(example = "Conference Room A")
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 255, message = "Name must not be longer than 255 characters")
        String name,
    @Schema(example = "10") @Positive(message = "Capacity must be greater than zero")
        int capacity) {
  public RoomRequest {
    name = name.trim();
  }
}
