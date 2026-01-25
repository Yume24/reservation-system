package com.ajaros.reservationsystem.rooms.services;

import com.ajaros.reservationsystem.equipment.services.EquipmentService;
import com.ajaros.reservationsystem.rooms.dtos.RoomResponse;
import com.ajaros.reservationsystem.rooms.dtos.RoomResponseWithEquipment;
import com.ajaros.reservationsystem.rooms.entities.Room;
import com.ajaros.reservationsystem.rooms.exceptions.AvailabilityException;
import com.ajaros.reservationsystem.rooms.exceptions.RoomNotFoundException;
import com.ajaros.reservationsystem.rooms.mappers.RoomMapper;
import com.ajaros.reservationsystem.rooms.repositories.RoomRepository;
import com.ajaros.reservationsystem.rooms.repositories.RoomSpecifications;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RoomService {
  private final RoomRepository roomRepository;
  private final RoomMapper roomMapper;
  private final EquipmentService equipmentService;

  public List<RoomResponse> getAllRooms() {
    return roomRepository.findAll().stream().map(roomMapper::toRoomResponse).toList();
  }

  public RoomResponse getRoomDtoById(Long id) {
    return roomMapper.toRoomResponse(getRoomById(id));
  }

  public Room getRoomById(Long id) {
    return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
  }

  public void validateRoomExists(Long id) {
    if (!roomRepository.existsById(id)) {
      throw new RoomNotFoundException(id);
    }
  }

  public List<RoomResponseWithEquipment> getMatchingRooms(
      Integer capacity, Instant from, Instant to, Set<String> equipmentNames) {
    validateDateRange(from, to);

    var spec = RoomSpecifications.matchingCriteria(capacity, from, to, equipmentNames);
    return roomRepository.findAll(spec, Sort.by("capacity")).stream()
        .map(roomMapper::toRoomResponseWithEquipment)
        .toList();
  }

  public RoomResponse createRoom(String name, int capacity) {
    var room = Room.builder().name(name).capacity(capacity).build();
    var createdRoom = roomRepository.save(room);
    return roomMapper.toRoomResponse(createdRoom);
  }

  @Transactional
  public RoomResponse updateRoom(Long id, String newName, int newCapacity) {
    var room = getRoomById(id);
    room.setName(newName);
    room.setCapacity(newCapacity);
    return roomMapper.toRoomResponse(room);
  }

  public void deleteRoom(Long id) {
    if (!roomRepository.existsById(id)) {
      throw new RoomNotFoundException(id);
    }
    roomRepository.deleteById(id);
  }

  @Transactional
  public void addEquipmentToRoom(Long roomId, Long equipmentId) {
    var room = getRoomById(roomId);
    var equipment = equipmentService.getEquipmentById(equipmentId);
    room.getEquipment().add(equipment);
  }

  @Transactional
  public void removeEquipmentFromRoom(Long roomId, Long equipmentId) {
    var room = getRoomById(roomId);
    var equipment = equipmentService.getEquipmentById(equipmentId);
    room.getEquipment().remove(equipment);
  }

  private void validateDateRange(Instant from, Instant to) {
    if (from.isAfter(to)) {
      throw new AvailabilityException("From date must be before to date");
    }
  }
}
