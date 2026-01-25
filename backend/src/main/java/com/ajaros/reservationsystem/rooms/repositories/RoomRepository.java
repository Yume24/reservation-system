package com.ajaros.reservationsystem.rooms.repositories;

import com.ajaros.reservationsystem.rooms.entities.Room;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

  @EntityGraph(attributePaths = {"equipment"})
  @NonNull List<Room> findAll(@NonNull Specification<Room> spec, @NonNull Sort sort);

  @EntityGraph(attributePaths = {"equipment"})
  Optional<Room> findWithEquipmentById(Long id);
}
