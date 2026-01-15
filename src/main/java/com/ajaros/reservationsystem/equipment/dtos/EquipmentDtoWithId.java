package com.ajaros.reservationsystem.equipment.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record EquipmentDtoWithId(@Positive Long id, @NotBlank String name) {}
