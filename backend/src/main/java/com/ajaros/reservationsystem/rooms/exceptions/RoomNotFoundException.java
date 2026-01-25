package com.ajaros.reservationsystem.rooms.exceptions;

import com.ajaros.reservationsystem.exceptions.ResourceNotFoundException;

public class RoomNotFoundException extends ResourceNotFoundException {

  public RoomNotFoundException(Long id) {
    super("Room", id);
  }
}
