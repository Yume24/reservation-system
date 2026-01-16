package com.ajaros.reservationsystem.reservations.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReservationExceptionHandler {
  @ExceptionHandler(InvalidReservationException.class)
  public ResponseEntity<Void> handleInvalidReservationException() {
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(ReservationNotFoundException.class)
  public ResponseEntity<Void> handleReservationNotFoundException() {
    return ResponseEntity.notFound().build();
  }
}
