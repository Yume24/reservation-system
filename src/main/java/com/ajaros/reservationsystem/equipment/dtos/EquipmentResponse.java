package com.ajaros.reservationsystem.equipment.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record EquipmentResponse(
    @Schema(example = "1") Long id,
    @Schema(example = "Projector") String name) {}
