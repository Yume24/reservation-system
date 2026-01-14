package com.ajaros.reservationsystem.rooms.repositories;

import com.ajaros.reservationsystem.rooms.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {}
