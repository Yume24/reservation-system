package com.ajaros.reservationsystem.auth.exceptions;

import com.ajaros.reservationsystem.exceptions.ConflictException;

public class UserAlreadyExistsException extends ConflictException {

  public UserAlreadyExistsException(String email) {
    super("User with email " + email + " already exists");
  }
}
