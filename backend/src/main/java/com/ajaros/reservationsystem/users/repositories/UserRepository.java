package com.ajaros.reservationsystem.users.repositories;

import com.ajaros.reservationsystem.users.entities.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
