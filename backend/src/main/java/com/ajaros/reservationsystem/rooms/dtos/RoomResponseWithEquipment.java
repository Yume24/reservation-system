package com.ajaros.reservationsystem.rooms.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record RoomResponseWithEquipment(
    @Schema(example = "1") long id,
    @Schema(example = "Conference Room A") String name,
    @Schema(example = "10") int capacity,
    @Schema(example = "[\"Projector\"]") List<String> equipmentNames) {}
