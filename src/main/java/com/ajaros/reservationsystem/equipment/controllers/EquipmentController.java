package com.ajaros.reservationsystem.equipment.controllers;

import com.ajaros.reservationsystem.equipment.dtos.EquipmentRequest;
import com.ajaros.reservationsystem.equipment.dtos.EquipmentResponse;
import com.ajaros.reservationsystem.equipment.services.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Equipment Management", description = "Endpoints for managing equipment")
public class EquipmentController {
  private final EquipmentService equipmentService;

  @Operation(
      summary = "Get all equipment",
      description = "Returns a list of all available equipment")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of equipment"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
      })
  @GetMapping
  public List<EquipmentResponse> getAllEquipment() {
    return equipmentService.getAllEquipment();
  }

  @Operation(
      summary = "Get equipment by ID",
      description = "Returns a single piece of equipment by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved equipment"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Equipment not found")
      })
  @GetMapping("/{id}")
  public EquipmentResponse getEquipmentById(@PathVariable String id) {
    return equipmentService.getEquipmentById(Long.valueOf(id));
  }

  @Operation(
      summary = "Create new equipment",
      description = "Creates a new piece of equipment in the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Equipment successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin only")
      })
  @RolesAllowed("ADMIN")
  @PostMapping
  public ResponseEntity<EquipmentResponse> createEquipment(
      @Valid @RequestBody EquipmentRequest request) throws URISyntaxException {
    var createdEquipment = equipmentService.createEquipment(request.name());
    return ResponseEntity.created(new URI("/equipment/" + createdEquipment.id()))
        .body(createdEquipment);
  }

  @Operation(summary = "Update equipment", description = "Updates equipment details by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Equipment successfully updated"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin only"),
        @ApiResponse(responseCode = "404", description = "Equipment not found")
      })
  @RolesAllowed("ADMIN")
  @PutMapping("/{id}")
  public EquipmentResponse updateEquipment(
      @Valid @RequestBody EquipmentRequest request, @PathVariable String id) {

    return equipmentService.updateEquipment(Long.valueOf(id), request.name());
  }

  @Operation(
      summary = "Delete equipment",
      description = "Deletes equipment from the system by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Equipment successfully deleted"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin only"),
        @ApiResponse(responseCode = "404", description = "Equipment not found")
      })
  @RolesAllowed("ADMIN")
  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteEquipment(@PathVariable String id) {
    equipmentService.deleteEquipment(Long.valueOf(id));
    return ResponseEntity.noContent().build();
  }
}
