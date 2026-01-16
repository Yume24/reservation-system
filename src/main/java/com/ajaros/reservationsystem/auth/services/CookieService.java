package com.ajaros.reservationsystem.auth.services;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
  public Cookie createRefreshTokenCookie(String token, int duration) {
    var cookie = new Cookie("refreshToken", token);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/auth/**");
    cookie.setMaxAge(duration);

    return cookie;
  }

  public Cookie deleteRefreshTokenCookie() {
    return createRefreshTokenCookie("", 0);
  }
}
