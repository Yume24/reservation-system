package com.ajaros.reservationsystem.reservations.controllers;

import com.ajaros.reservationsystem.reservations.dtos.ReservationResponse;
import com.ajaros.reservationsystem.reservations.services.ReservationService;
import jakarta.annotation.security.RolesAllowed;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RolesAllowed("VIEWER")
@RequestMapping("/reservations/viewer")
@AllArgsConstructor
public class ReservationViewerController {
  private final ReservationService reservationService;

  @GetMapping("/{roomId}")
  public List<ReservationResponse> getRoomReservations(
      @PathVariable Long roomId,
      @RequestParam(required = false, name = "from") Instant from,
      @RequestParam(required = false, name = "to") Instant to) {
    return reservationService.getFilteredReservations(null, from, to, roomId);
  }
}
