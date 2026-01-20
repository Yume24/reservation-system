package com.ajaros.reservationsystem.equipment.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EquipmentExceptionHandler {
  @ExceptionHandler(EquipmentNotFoundException.class)
  public ResponseEntity<Void> handleEquipmentNotFoundException() {
    return ResponseEntity.notFound().build();
  }
}
