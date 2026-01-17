package com.ajaros.reservationsystem.reservations.mappers;

import com.ajaros.reservationsystem.reservations.dtos.ReservationResponse;
import com.ajaros.reservationsystem.reservations.entites.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
  @Mapping(target = "roomId", expression = "java(reservation.getRoom().getId())")
  @Mapping(target = "userId", expression = "java(reservation.getUser().getId())")
  ReservationResponse toReservationResponse(Reservation reservation);
}
