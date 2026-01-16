package com.ajaros.reservationsystem.reservations.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Instant;

public record ReservationRequest(
    @Schema(example = "2026-01-20T10:00:00Z")
        @NotNull(message = "From date is required")
        @FutureOrPresent(message = "From date must be in the future")
        Instant fromDate,
    @Schema(example = "2026-01-20T12:00:00Z")
        @NotNull(message = "To date is required")
        @FutureOrPresent(message = "To date must be in the future")
        Instant toDate,
    @Schema(example = "1")
        @NotNull(message = "Room ID is required")
        @Positive(message = "Room ID must be greater than zero")
        long roomId) {}
