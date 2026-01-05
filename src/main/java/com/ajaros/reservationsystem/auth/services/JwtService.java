package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.auth.configuration.JwtConfiguration;
import com.ajaros.reservationsystem.auth.models.Jwt;
import com.ajaros.reservationsystem.users.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtService {
  private final JwtConfiguration jwtConfiguration;

  public Jwt generateAccessToken(User user) {
    return generateToken(user, jwtConfiguration.getAccessTokenExpiration());
  }

  private Jwt generateToken(User user, long expiration) {
    var claims =
        Jwts.claims()
            .subject(user.getEmail())
            .add("name", user.getName())
            .add("surname", user.getSurname())
            .add("role", user.getRole())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * expiration))
            .build();

    return new Jwt(claims, jwtConfiguration.getSecretKey());
  }

  public Jwt parseToken(String token) {
    try {
      var claims = getClaims(token);
      return new Jwt(claims, jwtConfiguration.getSecretKey());
    } catch (JwtException e) {
      return null;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(jwtConfiguration.getSecretKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
