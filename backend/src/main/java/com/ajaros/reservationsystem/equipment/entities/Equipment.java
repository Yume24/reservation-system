package com.ajaros.reservationsystem.equipment.entities;

import com.ajaros.reservationsystem.rooms.entities.Room;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "equipment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @ManyToMany
  @JoinTable(
      name = "room_equipment",
      joinColumns = @JoinColumn(name = "equipment_id"),
      inverseJoinColumns = @JoinColumn(name = "room_id"))
  @Builder.Default
  private Set<Room> rooms = new HashSet<>();
}
