package com.ajaros.reservationsystem.auth.controllers;

import com.ajaros.reservationsystem.auth.configuration.JwtConfiguration;
import com.ajaros.reservationsystem.auth.dtos.LoginRequest;
import com.ajaros.reservationsystem.auth.dtos.LoginResponse;
import com.ajaros.reservationsystem.auth.dtos.RegisterRequest;
import com.ajaros.reservationsystem.auth.dtos.RegisterResponse;
import com.ajaros.reservationsystem.auth.exceptions.InvalidTokenException;
import com.ajaros.reservationsystem.auth.exceptions.UserAlreadyExistsException;
import com.ajaros.reservationsystem.auth.services.AuthService;
import com.ajaros.reservationsystem.auth.services.RefreshTokenService;
import com.ajaros.reservationsystem.auth.utils.AuthTokensInfo;
import com.ajaros.reservationsystem.users.mappers.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final UserMapper userMapper;
  private final JwtConfiguration jwtConfiguration;
  private final RefreshTokenService refreshTokenService;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(
      @Valid @RequestBody RegisterRequest registerRequest) {
    var registerResponse = authService.registerUser(registerRequest);
    return ResponseEntity.status(201).body(registerResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    var authTokensInfo = authService.login(loginRequest);
    return getLoginResponseResponseEntity(authTokensInfo, response);
  }

  private @NonNull ResponseEntity<LoginResponse> getLoginResponseResponseEntity(
      AuthTokensInfo authTokensInfo, HttpServletResponse response) {
    var user = authTokensInfo.user();
    var accessToken = authTokensInfo.accessToken();
    var refreshToken = authTokensInfo.refreshToken();

    var refreshTokenCookie = new Cookie("refreshToken", refreshToken);

    refreshTokenCookie.setMaxAge((int) jwtConfiguration.getRefreshTokenExpiration());
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(true);
    refreshTokenCookie.setPath("/auth/refresh");

    response.addCookie(refreshTokenCookie);

    return ResponseEntity.ok(userMapper.toLoginResponse(user, accessToken));
  }

  @PostMapping("/refresh")
  public ResponseEntity<LoginResponse> refreshToken(
      @CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
    var authTokensInfo = refreshTokenService.issueNewRefreshToken(refreshToken);
    return getLoginResponseResponseEntity(authTokensInfo, response);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleUserAlreadyExists(
      UserAlreadyExistsException ex) {
    return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler({BadCredentialsException.class, InvalidTokenException.class})
  public ResponseEntity<Void> handleBadCredentialsException() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
