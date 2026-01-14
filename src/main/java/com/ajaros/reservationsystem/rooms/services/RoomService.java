package com.ajaros.reservationsystem.rooms.services;

import com.ajaros.reservationsystem.rooms.dtos.RoomDtoWithId;
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

  public List<RoomDtoWithId> getAllRooms() {
    return roomRepository.findAll().stream().map(roomMapper::toDtoWithId).toList();
  }

  public RoomDtoWithId getRoomById(Long id) {
    var room = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    return roomMapper.toDtoWithId(room);
  }

  public RoomDtoWithId createRoom(String name, int capacity) {
    var room = Room.builder().name(name).capacity(capacity).build();
    var createdRoom = roomRepository.save(room);
    return roomMapper.toDtoWithId(createdRoom);
  }

  @Transactional
  public RoomDtoWithId updateRoom(Long id, String newName, int newCapacity) {
    var room = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    room.setName(newName);
    room.setCapacity(newCapacity);
    var updatedRoom = roomRepository.save(room);
    return roomMapper.toDtoWithId(updatedRoom);
  }

  public void deleteRoom(Long id) {
    roomRepository.deleteById(id);
  }
}
