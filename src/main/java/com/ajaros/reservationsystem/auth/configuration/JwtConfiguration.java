package com.ajaros.reservationsystem.auth.configuration;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jwt")
@Data
public class JwtConfiguration {
  private String secret;
  private long accessTokenExpiration;

  public SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }
}
