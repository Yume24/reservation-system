package com.ajaros.reservationsystem.reservations.controllers;

import com.ajaros.reservationsystem.auth.annotations.AuthenticatedUserId;
import com.ajaros.reservationsystem.reservations.dtos.ReservationRequest;
import com.ajaros.reservationsystem.reservations.dtos.ReservationResponse;
import com.ajaros.reservationsystem.reservations.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations/user")
@RolesAllowed("USER")
@AllArgsConstructor
@Validated
@Tag(
    name = "User Reservations",
    description = "Endpoints for users to manage their own reservations")
public class ReservationUserController {

  private final ReservationService reservationService;

  @Operation(
      summary = "Get user reservations",
      description = "Returns a list of reservations for the currently authenticated user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved reservations"),
        @ApiResponse(responseCode = "400", description = "Invalid parameters"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User role required")
      })
  @GetMapping
  public List<ReservationResponse> getUserReservations(
      @AuthenticatedUserId Long userId,
      @RequestParam(required = false, name = "from") @FutureOrPresent Instant from,
      @RequestParam(required = false, name = "to") @FutureOrPresent Instant to,
      @RequestParam(required = false, name = "roomId") @Positive Long roomId) {
    return reservationService.getFilteredReservations(userId, from, to, roomId);
  }

  @Operation(
      summary = "Create a reservation",
      description = "Creates a new reservation for the user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Reservation successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input or room already reserved"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User role required")
      })
  @PostMapping
  public ResponseEntity<ReservationResponse> createReservation(
      @Valid @RequestBody ReservationRequest request, @AuthenticatedUserId Long userId) {
    var reservation =
        reservationService.createReservation(
            request.fromDate(), request.toDate(), request.roomId(), userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
  }

  @Operation(
      summary = "Delete user reservation",
      description = "Deletes a reservation owned by the currently authenticated user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Reservation successfully deleted"),
        @ApiResponse(responseCode = "400", description = "Invalid reservation ID"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User role required"),
        @ApiResponse(
            responseCode = "404",
            description = "Reservation not found or not owned by user")
      })
  @DeleteMapping("/{reservationId}")
  public ResponseEntity<Void> deleteReservation(
      @PathVariable @Positive Long reservationId, @AuthenticatedUserId Long userId) {
    reservationService.deleteReservation(reservationId, userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Update user reservation",
      description = "Updates a reservation owned by the currently authenticated user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Reservation successfully updated"),
        @ApiResponse(responseCode = "400", description = "Invalid input or IDs"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User role required"),
        @ApiResponse(
            responseCode = "404",
            description = "Reservation not found or not owned by user")
      })
  @PutMapping("/{reservationId}")
  public ResponseEntity<ReservationResponse> updateReservation(
      @PathVariable @Positive Long reservationId,
      @AuthenticatedUserId Long userId,
      @Valid @RequestBody ReservationRequest request) {
    var reservation =
        reservationService.updateReservation(
            reservationId, userId, request.roomId(), request.fromDate(), request.toDate());
    return ResponseEntity.ok(reservation);
  }
}
