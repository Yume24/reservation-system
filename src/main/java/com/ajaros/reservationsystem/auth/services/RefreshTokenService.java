package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.auth.entities.RefreshToken;
import com.ajaros.reservationsystem.auth.exceptions.InvalidTokenException;
import com.ajaros.reservationsystem.auth.repositories.RefreshTokenRepository;
import com.ajaros.reservationsystem.auth.utils.AuthTokensInfo;
import com.ajaros.reservationsystem.auth.utils.HashUtility;
import java.time.Instant;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtService jwtService;
  private final JwtDecoder jwtDecoder;

  public void saveRefreshToken(String refreshToken) {
    var creationDate = jwtService.getIssuedAtFromToken(refreshToken);
    var expirationDate = jwtService.getExpirationFromToken(refreshToken);

    var refreshTokenEntity =
        RefreshToken.builder()
            .token(HashUtility.hash(refreshToken))
            .creationDate(creationDate)
            .expirationDate(expirationDate)
            .build();

    refreshTokenRepository.save(refreshTokenEntity);
  }

  @Transactional
  public AuthTokensInfo issueNewRefreshToken(String refreshToken) {
    var jwtRefreshToken = jwtDecoder.decode(refreshToken);
    var token = validateRefreshToken(jwtRefreshToken);
    var user = jwtService.getUserFromToken(jwtRefreshToken);
    deleteRefreshToken(token);

    var newRefreshToken = jwtService.generateRefreshToken(user);
    var newAccessToken = jwtService.generateAccessToken(user);

    saveRefreshToken(newRefreshToken);

    return new AuthTokensInfo(newAccessToken, newRefreshToken, user);
  }

  private RefreshToken validateRefreshToken(Jwt refreshToken) {
    var token =
        refreshTokenRepository
            .findByToken(HashUtility.hash(refreshToken.getTokenValue()))
            .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));
    if (token.getCreationDate().isAfter(Instant.now()))
      throw new InvalidTokenException("Invalid refresh token");
    return token;
  }

  private void deleteRefreshToken(RefreshToken refreshToken) {
    refreshTokenRepository.delete(refreshToken);
  }
}
