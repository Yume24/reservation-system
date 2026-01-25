package com.ajaros.reservationsystem.rooms.controllers;

import com.ajaros.reservationsystem.rooms.dtos.RoomRequest;
import com.ajaros.reservationsystem.rooms.dtos.RoomResponse;
import com.ajaros.reservationsystem.rooms.dtos.RoomResponseWithEquipment;
import com.ajaros.reservationsystem.rooms.services.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/rooms")
@AllArgsConstructor
@Validated
@Tag(name = "Room Management", description = "Endpoints for managing rooms")
public class RoomController {
  private final RoomService roomService;

  @Operation(summary = "Get all rooms", description = "Returns a list of all available rooms")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of rooms"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
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
        @ApiResponse(responseCode = "400", description = "Invalid room ID"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Room not found")
      })
  @GetMapping("/{id}")
  public RoomResponse getRoomById(@PathVariable @Positive Long id) {
    return roomService.getRoomDtoById(id);
  }

  @Operation(
      summary = "Get available rooms",
      description =
          "Returns a list of rooms that are available in a given time slot and match the criteria")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of available rooms"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
      })
  @GetMapping("/available")
  public List<RoomResponseWithEquipment> getAvailableRooms(
      @RequestParam(name = "capacity", required = false) @Positive Integer capacity,
      @RequestParam(name = "from") @FutureOrPresent Instant from,
      @RequestParam(name = "to") @FutureOrPresent Instant to,
      @RequestParam(name = "equipmentNames", required = false)
          Set<@NotBlank @Size(max = 255) String> equipmentNames) {
    return roomService.getMatchingRooms(capacity, from, to, equipmentNames);
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
  public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {
    var room = roomService.createRoom(request.name(), request.capacity());
    var location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(room.id())
            .toUri();
    return ResponseEntity.created(location).body(room);
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
      @Valid @RequestBody RoomRequest request, @PathVariable @Positive Long id) {
    var newRoom = roomService.updateRoom(id, request.name(), request.capacity());
    return ResponseEntity.ok(newRoom);
  }

  @Operation(summary = "Delete a room", description = "Deletes a room from the system by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Room successfully deleted"),
        @ApiResponse(responseCode = "400", description = "Invalid room ID"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin only"),
        @ApiResponse(responseCode = "404", description = "Room not found")
      })
  @DeleteMapping("/{id}")
  @RolesAllowed("ADMIN")
  public ResponseEntity<Void> deleteRoom(@PathVariable @Positive Long id) {
    roomService.deleteRoom(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Add equipment to room",
      description = "Assigns an equipment to a specific room")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Equipment successfully added to room"),
        @ApiResponse(responseCode = "400", description = "Invalid input or IDs"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin only"),
        @ApiResponse(responseCode = "404", description = "Room or equipment not found")
      })
  @PostMapping("/{roomId}/equipment/{equipmentId}")
  @RolesAllowed("ADMIN")
  public ResponseEntity<Void> addEquipmentToRoom(
      @PathVariable @Positive Long roomId, @PathVariable @Positive Long equipmentId) {
    roomService.addEquipmentToRoom(roomId, equipmentId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(
      summary = "Remove equipment from room",
      description = "Removes an equipment assignment from a specific room")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "Equipment successfully removed from room"),
        @ApiResponse(responseCode = "400", description = "Invalid input or IDs"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin only"),
        @ApiResponse(responseCode = "404", description = "Room or equipment not found")
      })
  @DeleteMapping("/{roomId}/equipment/{equipmentId}")
  @RolesAllowed("ADMIN")
  public ResponseEntity<Void> removeEquipmentFromRoom(
      @PathVariable @Positive Long roomId, @PathVariable @Positive Long equipmentId) {
    roomService.removeEquipmentFromRoom(roomId, equipmentId);
    return ResponseEntity.noContent().build();
  }
}
