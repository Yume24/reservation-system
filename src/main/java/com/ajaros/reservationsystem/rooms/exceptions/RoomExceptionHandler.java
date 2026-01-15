package com.ajaros.reservationsystem.rooms.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoomExceptionHandler {
  @ExceptionHandler(RoomNotFoundException.class)
  public ResponseEntity<Void> handleRoomNotFoundException() {
    return ResponseEntity.notFound().build();
  }
}
