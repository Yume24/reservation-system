package com.ajaros.reservationsystem.equipment.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record EquipmentResponse(
    @Schema(example = "1") @Positive Long id,
    @Schema(example = "Projector") @NotBlank String name) {}
