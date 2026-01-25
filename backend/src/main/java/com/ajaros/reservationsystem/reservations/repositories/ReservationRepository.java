package com.ajaros.reservationsystem.reservations.repositories;

import com.ajaros.reservationsystem.reservations.entities.Reservation;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository
    extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

  @Override
  @EntityGraph(attributePaths = {"room", "user"})
  @NonNull List<Reservation> findAll(@NonNull Specification<Reservation> spec);

  @Override
  @EntityGraph(attributePaths = {"room", "user"})
  @NonNull Optional<Reservation> findById(Long id);

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
