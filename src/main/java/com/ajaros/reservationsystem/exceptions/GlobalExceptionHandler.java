package com.ajaros.reservationsystem.exceptions;

import com.ajaros.reservationsystem.users.exceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(
      MethodArgumentNotValidException ex) {
    var errors = new HashMap<String, String>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    var errors = new HashMap<String, String>();

    ex.getConstraintViolations()
        .forEach(
            violation -> {
              var propertyPath = violation.getPropertyPath().toString();
              var fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
              errors.put(fieldName, violation.getMessage());
            });

    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Void> handleUserNotFoundException() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Void> handleHttpMessageNotReadableException() {
    return ResponseEntity.badRequest().build();
  }
}
