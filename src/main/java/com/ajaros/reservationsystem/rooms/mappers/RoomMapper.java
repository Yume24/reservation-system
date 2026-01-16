package com.ajaros.reservationsystem.rooms.mappers;

import com.ajaros.reservationsystem.rooms.dtos.RoomResponse;
import com.ajaros.reservationsystem.rooms.entities.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

  RoomResponse toRoomResponse(Room room);
}
