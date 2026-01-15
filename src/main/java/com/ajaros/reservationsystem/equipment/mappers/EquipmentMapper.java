package com.ajaros.reservationsystem.equipment.mappers;

import com.ajaros.reservationsystem.equipment.dtos.EquipmentResponse;
import com.ajaros.reservationsystem.equipment.entities.Equipment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {
  EquipmentResponse toEquipmentResponse(Equipment equipment);
}
