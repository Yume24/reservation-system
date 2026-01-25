package com.ajaros.reservationsystem.equipment.services;

import com.ajaros.reservationsystem.equipment.dtos.EquipmentResponse;
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

  public List<EquipmentResponse> getAllEquipment() {
    return equipmentRepository.findAll().stream()
        .map(equipmentMapper::toEquipmentResponse)
        .toList();
  }

  public EquipmentResponse getEquipmentDtoById(Long id) {
    return equipmentMapper.toEquipmentResponse(getEquipmentById(id));
  }

  public Equipment getEquipmentById(Long id) {
    return equipmentRepository.findById(id).orElseThrow(() -> new EquipmentNotFoundException(id));
  }

  public EquipmentResponse createEquipment(String name) {
    var equipment = Equipment.builder().name(name).build();
    var createdEquipment = equipmentRepository.save(equipment);
    return equipmentMapper.toEquipmentResponse(createdEquipment);
  }

  @Transactional
  public EquipmentResponse updateEquipment(Long id, String newName) {
    var equipment = getEquipmentById(id);
    equipment.setName(newName);
    return equipmentMapper.toEquipmentResponse(equipmentRepository.save(equipment));
  }

  public void deleteEquipment(Long id) {
    equipmentRepository.deleteById(id);
  }
}
