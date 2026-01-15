package com.ajaros.reservationsystem.equipment.controllers;

import com.ajaros.reservationsystem.equipment.dtos.EquipmentDto;
import com.ajaros.reservationsystem.equipment.dtos.EquipmentDtoWithId;
import com.ajaros.reservationsystem.equipment.services.EquipmentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipment")
@AllArgsConstructor
public class EquipmentController {
  private final EquipmentService equipmentService;

  @GetMapping
  public List<EquipmentDtoWithId> getAllEquipment() {
    return equipmentService.getAllEquipment();
  }

  @GetMapping("/{id}")
  public EquipmentDtoWithId getEquipmentById(@PathVariable String id) {
    return equipmentService.getEquipmentById(Long.valueOf(id));
  }

  @RolesAllowed("ADMIN")
  @PostMapping
  public ResponseEntity<EquipmentDtoWithId> createEquipment(
      @Valid @RequestBody EquipmentDto request) throws URISyntaxException {
    var createdEquipment = equipmentService.createEquipment(request.name());
    return ResponseEntity.created(new URI("/equipment/" + createdEquipment.id()))
        .body(createdEquipment);
  }

  @RolesAllowed("ADMIN")
  @PutMapping("/{id}")
  public EquipmentDtoWithId updateEquipment(
      @Valid @RequestBody EquipmentDto request, @PathVariable String id) {

    return equipmentService.updateEquipment(Long.valueOf(id), request.name());
  }

  @RolesAllowed("ADMIN")
  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteEquipment(@PathVariable String id) {
    equipmentService.deleteEquipment(Long.valueOf(id));
    return ResponseEntity.noContent().build();
  }
}
