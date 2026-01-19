package com.ajaros.reservationsystem.rooms.repositories;

import com.ajaros.reservationsystem.rooms.entities.Room;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {
  @EntityGraph(attributePaths = {"equipment", "reservations"})
  @Query(
"""
        select room from Room room
        where (:capacity is null or  room.capacity >= :capacity)
        and not exists (
                select reservation from room.reservations reservation
                where reservation.fromDate < :to
                and reservation.toDate > :from
        )
        and (:equipmentNames is null or not exists (
                select equipment from Equipment equipment
                where equipment.name in :equipmentNames
                and equipment not in elements(room.equipment)
        ))
""")
  List<Room> findMatchingRooms(
      @Param("capacity") Integer capacity,
      @Param("from") Instant from,
      @Param("to") Instant to,
      @Param("equipmentNames") Set<String> equipmentNames);
}
