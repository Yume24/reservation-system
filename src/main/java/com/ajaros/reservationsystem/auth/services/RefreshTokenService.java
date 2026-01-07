package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.auth.entities.RefreshToken;
import com.ajaros.reservationsystem.auth.repositories.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  public void saveRefreshToken(String refreshToken) {
    var creationDate = jwtService.getIssuedAtFromToken(refreshToken);
    var expirationDate = jwtService.getExpirationFromToken(refreshToken);

    var refreshTokenEntity =
        RefreshToken.builder()
            .token(passwordEncoder.encode(refreshToken))
            .creationDate(creationDate)
            .expirationDate(expirationDate)
            .build();

    refreshTokenRepository.save(refreshTokenEntity);
  }
}
