package com.ajaros.reservationsystem.reservations.controllers;

import com.ajaros.reservationsystem.auth.services.JwtService;
import com.ajaros.reservationsystem.reservations.dtos.ReservationRequest;
import com.ajaros.reservationsystem.reservations.dtos.ReservationResponse;
import com.ajaros.reservationsystem.reservations.services.ReservationService;
import jakarta.annotation.security.RolesAllowed;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations/user")
@RolesAllowed("USER")
@AllArgsConstructor
public class ReservationUserController {
  private final ReservationService reservationService;
  private final JwtService jwtService;

  @GetMapping
  public List<ReservationResponse> getUserReservations(
      @AuthenticationPrincipal Jwt token,
      @RequestParam(required = false, name = "from") Instant from,
      @RequestParam(required = false, name = "to") Instant to,
      @RequestParam(required = false, name = "roomId") Long roomId) {
    var userId = jwtService.getUserIdFromToken(token);
    return reservationService.getFilteredReservations(userId, from, to, roomId);
  }

  @PostMapping
  public ResponseEntity<ReservationResponse> createReservation(
      @RequestBody ReservationRequest request, @AuthenticationPrincipal Jwt token) {
    var userId = jwtService.getUserIdFromToken(token);
    var reservation =
        reservationService.createReservation(
            request.fromDate(), request.toDate(), request.roomId(), userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
  }

  @DeleteMapping("/{reservationId}")
  public ResponseEntity<Void> deleteReservation(
      @PathVariable Long reservationId, @AuthenticationPrincipal Jwt token) {
    var userId = jwtService.getUserIdFromToken(token);
    reservationService.deleteReservation(reservationId, userId);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{reservationId}")
  public ResponseEntity<ReservationResponse> updateReservation(
      @PathVariable Long reservationId,
      @AuthenticationPrincipal Jwt token,
      @RequestBody ReservationRequest request) {
    var userId = jwtService.getUserIdFromToken(token);
    var reservation =
        reservationService.updateReservationEntity(
            reservationId, userId, request.roomId(), request.fromDate(), request.toDate());
    return ResponseEntity.ok(reservation);
  }
}
