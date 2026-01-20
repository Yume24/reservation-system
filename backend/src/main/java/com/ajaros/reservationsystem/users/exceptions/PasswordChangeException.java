package com.ajaros.reservationsystem.users.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordChangeException extends RuntimeException {
  public PasswordChangeException(String message) {
    super(message);
  }
}
