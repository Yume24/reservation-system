package com.ajaros.reservationsystem.equipment.exceptions;

import com.ajaros.reservationsystem.exceptions.ResourceNotFoundException;

public class EquipmentNotFoundException extends ResourceNotFoundException {

  public EquipmentNotFoundException(Long id) {
    super("Equipment", id);
  }
}
