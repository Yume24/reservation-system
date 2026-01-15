package com.ajaros.reservationsystem.rooms.services;

import com.ajaros.reservationsystem.equipment.services.EquipmentService;
import com.ajaros.reservationsystem.rooms.dtos.RoomResponse;
import com.ajaros.reservationsystem.rooms.entities.Room;
import com.ajaros.reservationsystem.rooms.exceptions.RoomNotFoundException;
import com.ajaros.reservationsystem.rooms.mappers.RoomMapper;
import com.ajaros.reservationsystem.rooms.repositories.RoomRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RoomService {
  private final RoomRepository roomRepository;
  private final RoomMapper roomMapper;
  private final EquipmentService equipmentService;

  public List<RoomResponse> getAllRooms() {
    return roomRepository.findAll().stream().map(roomMapper::toDtoWithId).toList();
  }

  public RoomResponse getRoomDtoById(Long id) {
    return roomMapper.toDtoWithId(getRoomById(id));
  }

  public Room getRoomById(Long id) {
    return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
  }

  public RoomResponse createRoom(String name, int capacity) {
    var room = Room.builder().name(name).capacity(capacity).build();
    var createdRoom = roomRepository.save(room);
    return roomMapper.toDtoWithId(createdRoom);
  }

  @Transactional
  public RoomResponse updateRoom(Long id, String newName, int newCapacity) {
    var room = getRoomById(id);
    room.setName(newName);
    room.setCapacity(newCapacity);
    var updatedRoom = roomRepository.save(room);
    return roomMapper.toDtoWithId(updatedRoom);
  }

  public void deleteRoom(Long id) {
    roomRepository.deleteById(id);
  }

  @Transactional
  public void addEquipmentToRoom(Long roomId, Long equipmentId) {
    var room = getRoomById(roomId);
    var equipment = equipmentService.getEquipmentById(equipmentId);

    room.getEquipment().add(equipment);

    roomRepository.save(room);
  }

  @Transactional
  public void removeEquipmentFromRoom(Long roomId, Long equipmentId) {
    var room = getRoomById(roomId);
    var equipment = equipmentService.getEquipmentById(equipmentId);

    room.getEquipment().remove(equipment);

    roomRepository.save(room);
  }
}
