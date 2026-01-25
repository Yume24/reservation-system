package com.ajaros.reservationsystem.rooms.exceptions;

import com.ajaros.reservationsystem.exceptions.BusinessException;

public class AvailabilityException extends BusinessException {

  public AvailabilityException(String message) {
    super(message);
  }
}
