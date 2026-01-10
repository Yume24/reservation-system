package com.ajaros.reservationsystem.auth.controllers;

import com.ajaros.reservationsystem.auth.configuration.JwtConfiguration;
import com.ajaros.reservationsystem.auth.dtos.LoginRequest;
import com.ajaros.reservationsystem.auth.dtos.LoginResponse;
import com.ajaros.reservationsystem.auth.dtos.RegisterRequest;
import com.ajaros.reservationsystem.auth.dtos.RegisterResponse;
import com.ajaros.reservationsystem.auth.services.AuthService;
import com.ajaros.reservationsystem.auth.services.CookieService;
import com.ajaros.reservationsystem.auth.services.RefreshTokenService;
import com.ajaros.reservationsystem.auth.utils.AuthTokensInfo;
import com.ajaros.reservationsystem.users.mappers.UserMapper;
import com.ajaros.reservationsystem.users.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final UserMapper userMapper;
  private final JwtConfiguration jwtConfiguration;
  private final RefreshTokenService refreshTokenService;
  private final CookieService cookieService;
  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(
      @Valid @RequestBody RegisterRequest registerRequest) {
    var registerResponse = userService.registerUser(registerRequest);
    return ResponseEntity.status(201).body(registerResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    var authTokensInfo = authService.login(loginRequest);
    return getLoginResponseResponseEntity(authTokensInfo, response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<LoginResponse> refreshToken(
      @CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
    var authTokensInfo = refreshTokenService.issueNewRefreshToken(refreshToken);
    return getLoginResponseResponseEntity(authTokensInfo, response);
  }

  private @NonNull ResponseEntity<LoginResponse> getLoginResponseResponseEntity(
      AuthTokensInfo authTokensInfo, HttpServletResponse response) {
    var user = authTokensInfo.user();
    var accessToken = authTokensInfo.accessToken();
    var refreshToken = authTokensInfo.refreshToken();

    var refreshTokenCookie =
        cookieService.createRefreshTokenCookie(
            refreshToken, (int) jwtConfiguration.getRefreshTokenExpiration());

    response.addCookie(refreshTokenCookie);

    return ResponseEntity.ok(userMapper.toLoginResponse(user, accessToken));
  }
}
