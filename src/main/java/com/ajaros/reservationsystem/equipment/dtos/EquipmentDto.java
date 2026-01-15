package com.ajaros.reservationsystem.equipment.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EquipmentDto(
    @NotBlank @Size(max = 255, message = "Name must not be longer than 255 characters")
        String name) {
  public EquipmentDto {
    name = name.trim();
  }
}
