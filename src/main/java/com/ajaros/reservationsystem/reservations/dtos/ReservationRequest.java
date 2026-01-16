package com.ajaros.reservationsystem.reservations.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Instant;

public record ReservationRequest(
    @NotNull(message = "From date is required")
        @FutureOrPresent(message = "From date must be in the future")
        Instant fromDate,
    @NotNull(message = "To date is required")
    @FutureOrPresent(message = "To date must be in the future")
    Instant toDate,
    @NotNull(message = "Room ID is required")
    @Positive(message = "Room ID must be greater than zero")
    long roomId) {}
