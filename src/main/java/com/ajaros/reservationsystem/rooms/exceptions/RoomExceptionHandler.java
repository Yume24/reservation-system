package com.ajaros.reservationsystem.rooms.exceptions;

import com.ajaros.reservationsystem.rooms.controllers.RoomController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = RoomController.class)
public class RoomExceptionHandler {
  @ExceptionHandler(RoomNotFoundException.class)
  public ResponseEntity<Void> handleRoomNotFoundException() {
    return ResponseEntity.notFound().build();
  }
}
