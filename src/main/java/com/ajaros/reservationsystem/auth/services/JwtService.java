package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.auth.configuration.JwtConfiguration;
import com.ajaros.reservationsystem.users.entities.User;
import com.ajaros.reservationsystem.users.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtService {
  private final JwtConfiguration jwtConfiguration;
  private final UserRepository userRepository;
  private final JwtDecoder jwtDecoder;

  public String generateAccessToken(User user) {
    return generateToken(user, jwtConfiguration.getAccessTokenExpiration());
  }

  public String generateRefreshToken(User user) {
    return generateToken(user, jwtConfiguration.getRefreshTokenExpiration());
  }

  public long getUserIdFromToken(Jwt token) {
    return (long) token.getClaims().get("id");
  }

  public Instant getExpirationFromToken(String token) {
    return getExpirationFromToken(jwtDecoder.decode(token));
  }

  public Instant getExpirationFromToken(Jwt token) {
    return token.getExpiresAt();
  }

  public Instant getIssuedAtFromToken(String token) {
    return getIssuedAtFromToken(jwtDecoder.decode(token));
  }

  public Instant getIssuedAtFromToken(Jwt token) {
    return token.getIssuedAt();
  }

  public User getUserFromToken(Jwt token) {
    var userId = getUserIdFromToken(token);
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User with id " + userId + " not found"));
  }

  private String generateToken(User user, long expiration) {
    var claims =
        Jwts.claims()
            .subject(user.getEmail())
            .add("id", user.getId())
            .add("roles", List.of(user.getRole()))
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * expiration))
            .build();

    return Jwts.builder().claims(claims).signWith(jwtConfiguration.getSecretKey()).compact();
  }
}
