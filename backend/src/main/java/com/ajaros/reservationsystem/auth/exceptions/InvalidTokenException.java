package com.ajaros.reservationsystem.auth.exceptions;

import com.ajaros.reservationsystem.exceptions.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {

  public InvalidTokenException(String message) {
    super(message);
  }
}
