package com.ajaros.reservationsystem.rooms.controllers;

import com.ajaros.reservationsystem.rooms.dtos.RoomDto;
import com.ajaros.reservationsystem.rooms.dtos.RoomDtoWithId;
import com.ajaros.reservationsystem.rooms.services.RoomService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
@AllArgsConstructor
public class RoomController {
  private final RoomService roomService;

  @GetMapping
  public List<RoomDtoWithId> getAllRooms() {
    return roomService.getAllRooms();
  }

  @GetMapping("/{id}")
  public RoomDtoWithId getRoomById(@PathVariable String id) {
    return roomService.getRoomById(Long.valueOf(id));
  }

  @PostMapping
  @RolesAllowed("ADMIN")
  public ResponseEntity<RoomDtoWithId> createRoom(@Valid @RequestBody RoomDto request)
      throws URISyntaxException {

    var room = roomService.createRoom(request.name(), request.capacity());
    return ResponseEntity.created(new URI("/rooms/" + room.id())).body(room);
  }

  @PutMapping("/{id}")
  @RolesAllowed("ADMIN")
  public ResponseEntity<RoomDtoWithId> updateRoom(
      @Valid @RequestBody RoomDto request, @PathVariable String id) {
    var newRoom = roomService.updateRoom(Long.valueOf(id), request.name(), request.capacity());
    return ResponseEntity.ok(newRoom);
  }

  @DeleteMapping("/{id}")
  @RolesAllowed("ADMIN")
  public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
    roomService.deleteRoom(Long.valueOf(id));
    return ResponseEntity.noContent().build();
  }
}
