package com.ajaros.reservationsystem.rooms.exceptions;

public class RoomNotFoundException extends RuntimeException {
  public RoomNotFoundException(Long id) {
    super("Room with id " + id + " not found");
  }
}
