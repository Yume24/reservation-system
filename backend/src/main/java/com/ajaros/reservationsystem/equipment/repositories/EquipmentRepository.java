package com.ajaros.reservationsystem.equipment.repositories;

import com.ajaros.reservationsystem.equipment.entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {}
