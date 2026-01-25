package com.ajaros.reservationsystem.exceptions;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {
    var errors = new HashMap<String, String>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    var response =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), "Validation Failed", "Invalid input data", errors);
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    var errors = new HashMap<String, String>();
    ex.getConstraintViolations()
        .forEach(
            violation -> {
              var propertyPath = violation.getPropertyPath().toString();
              var fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
              errors.put(fieldName, violation.getMessage());
            });

    var response =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), "Validation Failed", "Invalid input data", errors);
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    var response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
    var response =
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage());
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
    var response = new ErrorResponse(HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler({UnauthorizedException.class, BadCredentialsException.class})
  public ResponseEntity<ErrorResponse> handleUnauthorizedException() {
    var response =
        new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "Authentication failed");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
    var response = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Forbidden", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException() {
    var response =
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Malformed request body");
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    var response =
        new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage());
    return ResponseEntity.badRequest().body(response);
  }
}
