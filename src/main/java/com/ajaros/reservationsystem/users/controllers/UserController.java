package com.ajaros.reservationsystem.users.controllers;

import com.ajaros.reservationsystem.auth.services.JwtService;
import com.ajaros.reservationsystem.users.dtos.UpdatePasswordRequest;
import com.ajaros.reservationsystem.users.dtos.UpdateUserInformationRequest;
import com.ajaros.reservationsystem.users.dtos.UserInformation;
import com.ajaros.reservationsystem.users.mappers.UserMapper;
import com.ajaros.reservationsystem.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing user profile and password")
public class UserController {
  private final UserService userService;
  private final JwtService jwtService;
  private final UserMapper userMapper;

  @Operation(
      summary = "Update user information",
      description = "Updates the information of the currently authenticated user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "User information successfully updated"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
      })
  @PatchMapping
  public ResponseEntity<Void> updateUserInformation(
      @Valid @RequestBody UpdateUserInformationRequest request,
      @AuthenticationPrincipal Jwt token) {
    var userId = jwtService.getUserIdFromToken(token);
    userService.updateUserInformation(userId, request);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Get user information",
      description = "Returns the information of the currently authenticated user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user information"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
      })
  @GetMapping
  public UserInformation getUserInformation(@AuthenticationPrincipal Jwt token) {
    var user = jwtService.getUserFromToken(token);
    return userMapper.toUserInformation(user);
  }

  @Operation(
      summary = "Update user password",
      description = "Updates the password of the currently authenticated user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Password successfully updated"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid old password or new password format")
      })
  @PatchMapping("/password")
  public ResponseEntity<Void> updateUserPassword(
      @Valid @RequestBody UpdatePasswordRequest request, @AuthenticationPrincipal Jwt token) {
    var userId = jwtService.getUserIdFromToken(token);
    userService.updateUserPassword(userId, request.oldPassword(), request.newPassword());
    return ResponseEntity.noContent().build();
  }
}
