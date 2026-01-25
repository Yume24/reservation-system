package com.ajaros.reservationsystem.reservations.exceptions;

import com.ajaros.reservationsystem.exceptions.BusinessException;

public class InvalidReservationException extends BusinessException {

  public InvalidReservationException(String message) {
    super(message);
  }
}
