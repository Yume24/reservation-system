package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.auth.entities.RefreshToken;
import com.ajaros.reservationsystem.auth.exceptions.InvalidTokenException;
import com.ajaros.reservationsystem.auth.repositories.RefreshTokenRepository;
import com.ajaros.reservationsystem.auth.dtos.AuthTokensInfo;
import com.ajaros.reservationsystem.auth.utils.HashUtility;
import com.ajaros.reservationsystem.users.entities.User;
import com.ajaros.reservationsystem.users.mappers.UserMapper;
import java.time.Instant;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtService jwtService;
  private final UserMapper userMapper;

  public void saveRefreshToken(String refreshToken, User user) {
    var decodedToken = jwtService.decode(refreshToken);
    var creationDate = jwtService.getIssuedAtFromToken(decodedToken);
    var expirationDate = jwtService.getExpirationFromToken(decodedToken);

    var refreshTokenEntity =
        RefreshToken.builder()
            .token(HashUtility.hash(refreshToken))
            .creationDate(creationDate)
            .expirationDate(expirationDate)
            .user(user)
            .build();

    refreshTokenRepository.save(refreshTokenEntity);
  }

  @Transactional
  public AuthTokensInfo issueNewRefreshToken(String refreshToken) {
    var validatedRefreshToken = validateRefreshToken(refreshToken);
    var user = validatedRefreshToken.getUser();
    deleteRefreshToken(validatedRefreshToken);

    var newRefreshToken = jwtService.generateRefreshToken(user);
    var newAccessToken = jwtService.generateAccessToken(user);

    saveRefreshToken(newRefreshToken, user);

    return new AuthTokensInfo(newAccessToken, newRefreshToken, userMapper.toUserInformation(user));
  }

  @Transactional
  public void logout(String refreshToken) {
    var validatedRefreshToken = validateRefreshToken(refreshToken);
    deleteRefreshToken(validatedRefreshToken);
  }

  private RefreshToken validateRefreshToken(Jwt refreshToken) {
    var expirationDate = jwtService.getExpirationFromToken(refreshToken);
    if (expirationDate.isBefore(Instant.now()))
      throw new InvalidTokenException("Expired refresh token");
    return refreshTokenRepository
        .findByToken(HashUtility.hash(refreshToken.getTokenValue()))
        .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));
  }

  private RefreshToken validateRefreshToken(String refreshToken) {
    return validateRefreshToken(jwtService.decode(refreshToken));
  }

  private void deleteRefreshToken(RefreshToken refreshToken) {
    refreshTokenRepository.delete(refreshToken);
  }
}
