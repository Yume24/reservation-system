package com.ajaros.reservationsystem.reservations.repositories;

import com.ajaros.reservationsystem.reservations.entites.Reservation;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
  @Query(
"""
SELECT r FROM Reservation r
WHERE
(:from IS NULL OR :from >= r.fromDate)
AND (:to IS NULL OR :to <= r.toDate)
AND (:roomId IS NULL OR :roomId = r.room.id)
AND (:userId IS NULL OR :userId = r.user.id)
""")
  List<Reservation> findFiltered(
      @Param("from") Instant from,
      @Param("to") Instant to,
      @Param("room_id") Long roomId,
      @Param("user_id") Long userId);
}
