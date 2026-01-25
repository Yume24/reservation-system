package com.ajaros.reservationsystem.rooms.entities;

import com.ajaros.reservationsystem.equipment.entities.Equipment;
import com.ajaros.reservationsystem.reservations.entities.Reservation;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "rooms")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "capacity")
  private Integer capacity;

  @ManyToMany
  @JoinTable(
      name = "room_equipment",
      joinColumns = @JoinColumn(name = "room_id"),
      inverseJoinColumns = @JoinColumn(name = "equipment_id"))
  @Builder.Default
  private Set<Equipment> equipment = new HashSet<>();

  @OneToMany(mappedBy = "room")
  @Builder.Default
  private Set<Reservation> reservations = new HashSet<>();
}
