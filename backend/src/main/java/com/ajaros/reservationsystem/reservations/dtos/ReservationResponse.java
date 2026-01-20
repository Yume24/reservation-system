package com.ajaros.reservationsystem.reservations.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Builder;

@Builder
public record ReservationResponse(
    @Schema(example = "1") long id,
    @Schema(example = "2026-01-20T10:00:00Z") Instant fromDate,
    @Schema(example = "2026-01-20T12:00:00Z") Instant toDate,
    @Schema(example = "1") long roomId,
    @Schema(example = "1") long userId) {}
