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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Validated
@Tag(
    name = "Authentication",
    description = "Endpoints for user authentication and token management")
public class AuthController {
  private final AuthService authService;
  private final UserMapper userMapper;
  private final JwtConfiguration jwtConfiguration;
  private final RefreshTokenService refreshTokenService;
  private final CookieService cookieService;
  private final UserService userService;

  @Operation(summary = "Register a new user", description = "Creates a new user account")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "User successfully registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
      })
  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(
      @Valid @RequestBody RegisterRequest registerRequest) {
    var registerResponse = userService.registerUser(registerRequest);
    return ResponseEntity.status(201).body(registerResponse);
  }

  @Operation(
      summary = "Login",
      description =
          "Authenticates a user and returns access token in body and refresh token in cookie")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
      })
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    var authTokensInfo = authService.login(loginRequest);
    return getLoginResponseResponseEntity(authTokensInfo, response);
  }

  @Operation(
      summary = "Refresh token",
      description = "Issues a new access token using the refresh token from cookie")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Token successfully refreshed"),
        @ApiResponse(responseCode = "400", description = "Missing or invalid refresh token"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
      })
  @PostMapping("/refresh")
  public ResponseEntity<LoginResponse> refreshToken(
      @CookieValue("refreshToken") @NotBlank String refreshToken, HttpServletResponse response) {
    var authTokensInfo = refreshTokenService.issueNewRefreshToken(refreshToken);
    return getLoginResponseResponseEntity(authTokensInfo, response);
  }

  @Operation(
      summary = "Logout",
      description = "Invalidates the refresh token and clears the cookie")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Successfully logged out"),
        @ApiResponse(responseCode = "400", description = "Missing refresh token"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
      })
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @CookieValue("refreshToken") @NotBlank String refreshToken, HttpServletResponse response) {
    refreshTokenService.logout(refreshToken);
    var cookie = cookieService.deleteRefreshTokenCookie();
    response.addCookie(cookie);
    return ResponseEntity.noContent().build();
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
