package com.ajaros.reservationsystem.exceptions;

public abstract class ConflictException extends RuntimeException {

  protected ConflictException(String message) {
    super(message);
  }
}
