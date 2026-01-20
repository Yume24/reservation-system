package com.ajaros.reservationsystem.reservations.controllers;

import com.ajaros.reservationsystem.reservations.dtos.ReservationResponse;
import com.ajaros.reservationsystem.reservations.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RolesAllowed("VIEWER")
@RequestMapping("/reservations/viewer")
@AllArgsConstructor
@Validated
@Tag(name = "Reservation Viewer", description = "Endpoints for viewing reservations")
public class ReservationViewerController {
  private final ReservationService reservationService;

  @Operation(
      summary = "Get room reservations",
      description =
          "Returns a list of reservations for a specific room within an optional time range")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved reservations"),
        @ApiResponse(responseCode = "400", description = "Invalid room ID or parameters"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Viewer role required"),
        @ApiResponse(responseCode = "404", description = "Room not found")
      })
  @GetMapping("/{roomId}")
  public List<ReservationResponse> getRoomReservations(
      @PathVariable @Positive Long roomId,
      @RequestParam(required = false, name = "from") @FutureOrPresent Instant from,
      @RequestParam(required = false, name = "to") @FutureOrPresent Instant to) {
    return reservationService.getFilteredReservations(null, from, to, roomId);
  }
}
