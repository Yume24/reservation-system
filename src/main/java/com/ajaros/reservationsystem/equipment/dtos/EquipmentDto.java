package com.ajaros.reservationsystem.equipment.dtos;

import jakarta.validation.constraints.NotBlank;

public record EquipmentDto(@NotBlank String name) {}
