package com.ajaros.reservationsystem.equipment.services;

import com.ajaros.reservationsystem.equipment.dtos.EquipmentDtoWithId;
import com.ajaros.reservationsystem.equipment.entities.Equipment;
import com.ajaros.reservationsystem.equipment.exceptions.EquipmentNotFoundException;
import com.ajaros.reservationsystem.equipment.mappers.EquipmentMapper;
import com.ajaros.reservationsystem.equipment.repositories.EquipmentRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class EquipmentService {
  private final EquipmentRepository equipmentRepository;
  private final EquipmentMapper equipmentMapper;

  public List<EquipmentDtoWithId> getAllEquipment() {
    return equipmentRepository.findAll().stream().map(equipmentMapper::toDtoWithId).toList();
  }

  public EquipmentDtoWithId getEquipmentById(Long id) {
    var equipment =
        equipmentRepository.findById(id).orElseThrow(() -> new EquipmentNotFoundException(id));
    return equipmentMapper.toDtoWithId(equipment);
  }

  public EquipmentDtoWithId createEquipment(String name) {
    var equipment = Equipment.builder().name(name).build();
    var createdEquipment = equipmentRepository.save(equipment);
    return equipmentMapper.toDtoWithId(createdEquipment);
  }

  @Transactional
  public EquipmentDtoWithId updateEquipment(Long id, String newName) {
    var equipment =
        equipmentRepository.findById(id).orElseThrow(() -> new EquipmentNotFoundException(id));
    equipment.setName(newName);
    var updatedEquipment = equipmentRepository.save(equipment);
    return equipmentMapper.toDtoWithId(updatedEquipment);
  }

  public void deleteEquipment(Long id) {
    equipmentRepository.deleteById(id);
  }
}
