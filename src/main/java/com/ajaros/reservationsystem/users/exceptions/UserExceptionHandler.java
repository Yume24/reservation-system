package com.ajaros.reservationsystem.users.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {
  @ExceptionHandler(PasswordChangeException.class)
  public ResponseEntity<Void> handlePasswordChangeException() {
    return ResponseEntity.badRequest().build();
  }
}
