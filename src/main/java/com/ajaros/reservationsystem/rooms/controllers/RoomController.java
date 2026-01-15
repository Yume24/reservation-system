package com.ajaros.reservationsystem.rooms.controllers;

import com.ajaros.reservationsystem.rooms.dtos.RoomRequest;
import com.ajaros.reservationsystem.rooms.dtos.RoomResponse;
import com.ajaros.reservationsystem.rooms.services.RoomService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
@AllArgsConstructor
@Tag(name = "Room Management", description = "Endpoints for managing rooms")
public class RoomController {
  private final RoomService roomService;

  @Operation(summary = "Get all rooms", description = "Returns a list of all available rooms")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of rooms"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
      })
  @GetMapping
  public List<RoomResponse> getAllRooms() {
    return roomService.getAllRooms();
  }

  @Operation(summary = "Get room by ID", description = "Returns a single room by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved room"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Room not found")
      })
  @GetMapping("/{id}")
  public RoomResponse getRoomById(@PathVariable Long id) {
    return roomService.getRoomDtoById(id);
  }

  @Operation(summary = "Create a new room", description = "Creates a new room in the system")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Room successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin only")
      })
  @PostMapping
  @RolesAllowed("ADMIN")
  public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request)
      throws URISyntaxException {

    var room = roomService.createRoom(request.name(), request.capacity());
    return ResponseEntity.created(new URI("/rooms/" + room.id())).body(room);
  }

  @Operation(summary = "Update an existing room", description = "Updates room details by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Room successfully updated"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin only"),
        @ApiResponse(responseCode = "404", description = "Room not found")
      })
  @PutMapping("/{id}")
  @RolesAllowed("ADMIN")
  public ResponseEntity<RoomResponse> updateRoom(
      @Valid @RequestBody RoomRequest request, @PathVariable Long id) {
    var newRoom = roomService.updateRoom(id, request.name(), request.capacity());
    return ResponseEntity.ok(newRoom);
  }

  @Operation(summary = "Delete a room", description = "Deletes a room from the system by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Room successfully deleted"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin only"),
        @ApiResponse(responseCode = "404", description = "Room not found")
      })
  @DeleteMapping("/{id}")
  @RolesAllowed("ADMIN")
  public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
    roomService.deleteRoom(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{roomId}/equipment/{equipmentId}")
  @RolesAllowed("ADMIN")
  public ResponseEntity<Void> addEquipmentToRoom(
      @PathVariable Long roomId, @PathVariable Long equipmentId) {
    roomService.addEquipmentToRoom(roomId, equipmentId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/{roomId}/equipment/{equipmentId}")
  @RolesAllowed("ADMIN")
  public ResponseEntity<Void> removeEquipmentFromRoom(
      @PathVariable Long roomId, @PathVariable Long equipmentId) {
    roomService.removeEquipmentFromRoom(roomId, equipmentId);
    return ResponseEntity.noContent().build();
  }
}
