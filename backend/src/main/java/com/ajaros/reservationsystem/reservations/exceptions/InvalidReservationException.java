package com.ajaros.reservationsystem.reservations.exceptions;

public class InvalidReservationException extends RuntimeException {
  public InvalidReservationException(String message) {
    super(message);
  }
}
