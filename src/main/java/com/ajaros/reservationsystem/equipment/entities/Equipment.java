package com.ajaros.reservationsystem.equipment.entities;

import com.ajaros.reservationsystem.rooms.entities.Room;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Equipment")
public class Equipment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @ManyToMany
  @JoinTable(
      name = "Room_equipment",
      joinColumns = @JoinColumn(name = "equipment_id"),
      inverseJoinColumns = @JoinColumn(name = "room_id"))
  private Set<Room> rooms = new HashSet<>();
}
