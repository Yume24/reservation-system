package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.auth.configuration.JwtConfiguration;
import com.ajaros.reservationsystem.users.entities.User;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtService {
  private final JwtConfiguration jwtConfiguration;

  public String generateAccessToken(User user) {
    return generateToken(user, jwtConfiguration.getAccessTokenExpiration());
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
