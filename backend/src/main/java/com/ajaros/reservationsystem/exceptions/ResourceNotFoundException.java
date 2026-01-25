package com.ajaros.reservationsystem.exceptions;

public abstract class ResourceNotFoundException extends RuntimeException {

  protected ResourceNotFoundException(String resourceName, Long id) {
    super(resourceName + " with id " + id + " not found");
  }

  protected ResourceNotFoundException(String message) {
    super(message);
  }
}
