package com.ajaros.reservationsystem.reservations.controllers;

import com.ajaros.reservationsystem.reservations.dtos.ReservationRequest;
import com.ajaros.reservationsystem.reservations.dtos.ReservationResponse;
import com.ajaros.reservationsystem.reservations.services.ReservationService;
import jakarta.annotation.security.RolesAllowed;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations/manager")
@RolesAllowed("MANAGER")
@AllArgsConstructor
public class ReservationManagerController {
  private final ReservationService reservationService;

  @GetMapping
  public List<ReservationResponse> getAllReservations(
      @RequestParam(required = false, name = "from") Instant from,
      @RequestParam(required = false, name = "to") Instant to,
      @RequestParam(required = false, name = "roomId") Long roomId,
      @RequestParam(required = false, name = "userId") Long userId) {
    return reservationService.getFilteredReservations(userId, from, to, roomId);
  }

  @DeleteMapping("/{reservationId}")
  public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
    reservationService.deleteReservation(reservationId);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{reservationId}")
  public ResponseEntity<ReservationResponse> updateReservation(
      @PathVariable Long reservationId, @RequestBody ReservationRequest request) {
    var reservation =
        reservationService.updateReservationEntity(
            reservationId, request.roomId(), request.fromDate(), request.toDate());
    return ResponseEntity.ok(reservation);
  }
}
