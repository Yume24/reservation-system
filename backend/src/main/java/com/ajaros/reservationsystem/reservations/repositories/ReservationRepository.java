package com.ajaros.reservationsystem.reservations.repositories;

import com.ajaros.reservationsystem.reservations.entities.Reservation;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository
    extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

  @Query(
      """
      SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
      FROM Reservation r
      WHERE r.room.id = :roomId
        AND r.fromDate < :to
        AND r.toDate > :from
        AND (:excludeId IS NULL OR r.id != :excludeId)
      """)
  boolean existsConflicting(
      @Param("roomId") Long roomId,
      @Param("from") Instant from,
      @Param("to") Instant to,
      @Param("excludeId") Long excludeId);
}
