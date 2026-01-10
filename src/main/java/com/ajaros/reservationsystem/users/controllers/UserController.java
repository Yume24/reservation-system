package com.ajaros.reservationsystem.users.controllers;

import com.ajaros.reservationsystem.auth.services.JwtService;
import com.ajaros.reservationsystem.users.dtos.UpdatePasswordRequest;
import com.ajaros.reservationsystem.users.dtos.UpdateUserInformationRequest;
import com.ajaros.reservationsystem.users.dtos.UserInformation;
import com.ajaros.reservationsystem.users.mappers.UserMapper;
import com.ajaros.reservationsystem.users.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
  private final UserService userService;
  private final JwtService jwtService;
  private final UserMapper userMapper;

  @PatchMapping
  public ResponseEntity<Void> updateUserInformation(
      @Valid @RequestBody UpdateUserInformationRequest request,
      @AuthenticationPrincipal Jwt token) {
    var userId = jwtService.getUserIdFromToken(token);
    userService.updateUserInformation(userId, request);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public UserInformation getUserInformation(@AuthenticationPrincipal Jwt token) {
    var user = jwtService.getUserFromToken(token);
    return userMapper.toUserInformation(user);
  }

  @PatchMapping("/password")
  public ResponseEntity<Void> updateUserPassword(
      @Valid @RequestBody UpdatePasswordRequest request, @AuthenticationPrincipal Jwt token) {
    var userId = jwtService.getUserIdFromToken(token);
    userService.updateUserPassword(userId, request.oldPassword(), request.newPassword());
    return ResponseEntity.noContent().build();
  }
}
