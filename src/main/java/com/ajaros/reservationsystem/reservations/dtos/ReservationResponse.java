package com.ajaros.reservationsystem.reservations.dtos;

import java.time.Instant;
import lombok.Builder;

@Builder
public record ReservationResponse(
    long id, Instant fromDate, Instant toDate, long roomId, long userId) {}
