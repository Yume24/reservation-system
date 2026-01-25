package com.ajaros.reservationsystem.users.exceptions;

import com.ajaros.reservationsystem.exceptions.BusinessException;

public class PasswordChangeException extends BusinessException {

  public PasswordChangeException(String message) {
    super(message);
  }
}
