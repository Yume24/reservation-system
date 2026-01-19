package com.ajaros.reservationsystem.rooms.mappers;

import com.ajaros.reservationsystem.rooms.dtos.RoomResponse;
import com.ajaros.reservationsystem.rooms.dtos.RoomResponseWithEquipment;
import com.ajaros.reservationsystem.rooms.entities.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

  RoomResponse toRoomResponse(Room room);

  @Mapping(
      target = "equipmentNames",
      expression =
          "java(room.getEquipment().stream().map(com.ajaros.reservationsystem.equipment.entities.Equipment::getName).toList())")
  RoomResponseWithEquipment toRoomResponseWithEquipment(Room room);
}
