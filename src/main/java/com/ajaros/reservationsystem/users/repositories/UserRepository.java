package com.ajaros.reservationsystem.users.repositories;

import com.ajaros.reservationsystem.users.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByEmail(String email);

  @Modifying
  @Transactional
  @Query("UPDATE User u SET u.name = :name, u.surname = :surname WHERE u.id = :id")
  void updateUserInformation(
      @Param("id") long id, @Param("name") String name, @Param("surname") String surname);
}
