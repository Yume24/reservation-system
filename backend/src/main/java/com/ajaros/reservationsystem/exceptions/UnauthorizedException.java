package com.ajaros.reservationsystem.exceptions;

public abstract class UnauthorizedException extends RuntimeException {

  protected UnauthorizedException(String message) {
    super(message);
  }
}
