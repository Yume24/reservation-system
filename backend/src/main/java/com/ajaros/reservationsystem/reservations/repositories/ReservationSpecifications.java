package com.ajaros.reservationsystem.reservations.repositories;

import com.ajaros.reservationsystem.reservations.entities.Reservation;
import java.time.Instant;
import org.springframework.data.jpa.domain.Specification;

public final class ReservationSpecifications {

  private ReservationSpecifications() {}

  public static Specification<Reservation> hasUserId(Long userId) {
    return (root, _, cb) -> userId == null ? null : cb.equal(root.get("user").get("id"), userId);
  }

  public static Specification<Reservation> hasRoomId(Long roomId) {
    return (root, _, cb) -> roomId == null ? null : cb.equal(root.get("room").get("id"), roomId);
  }

  public static Specification<Reservation> startsAfterOrAt(Instant from) {
    return (root, _, cb) -> from == null ? null : cb.lessThan(root.get("toDate"), from).not();
  }

  public static Specification<Reservation> endsBefore(Instant to) {
    return (root, _, cb) -> to == null ? null : cb.greaterThan(root.get("fromDate"), to).not();
  }

  public static Specification<Reservation> excludeId(Long excludeId) {
    return (root, _, cb) -> excludeId == null ? null : cb.notEqual(root.get("id"), excludeId);
  }

  public static Specification<Reservation> filtered(
      Instant from, Instant to, Long roomId, Long userId, Long excludeId) {
    return Specification.where(startsAfterOrAt(from))
        .and(endsBefore(to))
        .and(hasRoomId(roomId))
        .and(hasUserId(userId))
        .and(excludeId(excludeId));
  }
}
