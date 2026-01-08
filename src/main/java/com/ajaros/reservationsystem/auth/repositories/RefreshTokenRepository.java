package com.ajaros.reservationsystem.auth.repositories;

import com.ajaros.reservationsystem.auth.entities.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);
}
