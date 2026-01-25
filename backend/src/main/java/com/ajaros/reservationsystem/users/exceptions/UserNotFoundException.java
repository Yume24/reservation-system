package com.ajaros.reservationsystem.users.exceptions;

import com.ajaros.reservationsystem.exceptions.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {

  public UserNotFoundException() {
    super("User not found");
  }
}
