package com.ajaros.reservationsystem.rooms.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record RoomDto(
    @NotBlank(message = "Name cannot be blank") String name,
    @Positive(message = "Capacity must be greater than zero") int capacity) {}
