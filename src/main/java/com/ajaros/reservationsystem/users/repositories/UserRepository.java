package com.ajaros.reservationsystem.users.repositories;

import com.ajaros.reservationsystem.users.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
