package com.ajaros.reservationsystem.equipment.exceptions;

public class EquipmentNotFoundException extends RuntimeException {
  public EquipmentNotFoundException(Long id) {
    super("Equipment with id " + id + " not found");
  }
}
