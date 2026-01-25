package com.ajaros.reservationsystem.exceptions;

public abstract class BusinessException extends RuntimeException {

  protected BusinessException(String message) {
    super(message);
  }
}
