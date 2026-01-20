package com.ajaros.reservationsystem.equipment.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EquipmentRequest(
    @Schema(example = "Projector")
        @NotBlank
        @Size(max = 255, message = "Name must not be longer than 255 characters")
        String name) {
  public EquipmentRequest {
    name = name.trim();
  }
}
