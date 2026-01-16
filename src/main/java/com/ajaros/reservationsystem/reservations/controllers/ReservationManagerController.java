package com.ajaros.reservationsystem.reservations.controllers;

import com.ajaros.reservationsystem.reservations.dtos.ReservationRequest;
import com.ajaros.reservationsystem.reservations.dtos.ReservationResponse;
import com.ajaros.reservationsystem.reservations.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations/manager")
@RolesAllowed("MANAGER")
@AllArgsConstructor
@Tag(
    name = "Reservation Management",
    description = "Endpoints for managers to oversee reservations")
public class ReservationManagerController {
  private final ReservationService reservationService;

  @Operation(
      summary = "Get all reservations",
      description = "Returns a list of all reservations with optional filters")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved reservations"),
        @ApiResponse(responseCode = "400", description = "Invalid parameters"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Manager role required")
      })
  @GetMapping
  public List<ReservationResponse> getAllReservations(
      @RequestParam(required = false, name = "from") Instant from,
      @RequestParam(required = false, name = "to") Instant to,
      @RequestParam(required = false, name = "roomId") Long roomId,
      @RequestParam(required = false, name = "userId") Long userId) {
    return reservationService.getFilteredReservations(userId, from, to, roomId);
  }

  @Operation(summary = "Delete a reservation", description = "Deletes a reservation by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Reservation successfully deleted"),
        @ApiResponse(responseCode = "400", description = "Invalid reservation ID"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Manager role required"),
        @ApiResponse(responseCode = "404", description = "Reservation not found")
      })
  @DeleteMapping("/{reservationId}")
  public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
    reservationService.deleteReservation(reservationId);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Update a reservation",
      description = "Updates reservation details by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Reservation successfully updated"),
        @ApiResponse(responseCode = "400", description = "Invalid input or IDs"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Manager role required"),
        @ApiResponse(responseCode = "404", description = "Reservation not found")
      })
  @PutMapping("/{reservationId}")
  public ResponseEntity<ReservationResponse> updateReservation(
      @PathVariable Long reservationId, @Valid @RequestBody ReservationRequest request) {
    var reservation =
        reservationService.updateReservationEntity(
            reservationId, request.roomId(), request.fromDate(), request.toDate());
    return ResponseEntity.ok(reservation);
  }
}
