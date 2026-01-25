package com.ajaros.reservationsystem.rooms.repositories;

import com.ajaros.reservationsystem.rooms.entities.Room;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Subquery;
import java.time.Instant;
import java.util.Set;
import org.springframework.data.jpa.domain.Specification;

public final class RoomSpecifications {

  private RoomSpecifications() {}

  public static Specification<Room> hasMinCapacity(Integer capacity) {
    return (root, _, cb) ->
        capacity == null ? null : cb.greaterThanOrEqualTo(root.get("capacity"), capacity);
  }

  public static Specification<Room> isAvailableBetween(Instant from, Instant to) {
    return (root, query, cb) -> {
      if (from == null || to == null) {
        return null;
      }

      Subquery<Long> subquery = query.subquery(Long.class);
      var reservation = subquery.from(root.getModel()).join("reservations", JoinType.INNER);

      subquery
          .select(cb.literal(1L))
          .where(
              cb.and(
                  cb.equal(reservation.getParent().get("id"), root.get("id")),
                  cb.lessThan(reservation.get("fromDate"), to),
                  cb.greaterThan(reservation.get("toDate"), from)));

      return cb.not(cb.exists(subquery));
    };
  }

  public static Specification<Room> hasAllEquipment(Set<String> equipmentNames) {
    return (root, query, cb) -> {
      if (equipmentNames == null || equipmentNames.isEmpty()) {
        return null;
      }

      Subquery<Long> subquery = query.subquery(Long.class);
      var roomEquipment = subquery.from(root.getModel());
      var equipment = roomEquipment.join("equipment", JoinType.INNER);

      subquery
          .select(cb.countDistinct(equipment.get("name")))
          .where(
              cb.and(
                  cb.equal(roomEquipment.get("id"), root.get("id")),
                  equipment.get("name").in(equipmentNames)));

      return cb.equal(subquery, (long) equipmentNames.size());
    };
  }

  public static Specification<Room> matchingCriteria(
      Integer capacity, Instant from, Instant to, Set<String> equipmentNames) {
    return Specification.where(hasMinCapacity(capacity))
        .and(isAvailableBetween(from, to))
        .and(hasAllEquipment(equipmentNames));
  }
}
