package com.ajaros.reservationsystem.auth.models;

import com.ajaros.reservationsystem.users.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;

public record Jwt(Claims claims, SecretKey secretKey) {
  public boolean isExpired() {
    return claims.getExpiration().before(new Date());
  }

  public String getUser() {
    return claims.getSubject();
  }

  public Role getRole() {
    return Role.valueOf(claims.get("role").toString());
  }

  @Override
  public String toString() {
    return Jwts.builder().claims(claims).signWith(secretKey).compact();
  }
}
