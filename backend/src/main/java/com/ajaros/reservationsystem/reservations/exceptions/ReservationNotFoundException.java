package com.ajaros.reservationsystem.reservations.exceptions;

import com.ajaros.reservationsystem.exceptions.ResourceNotFoundException;

public class ReservationNotFoundException extends ResourceNotFoundException {

  public ReservationNotFoundException(Long id) {
    super("Reservation", id);
  }
}
