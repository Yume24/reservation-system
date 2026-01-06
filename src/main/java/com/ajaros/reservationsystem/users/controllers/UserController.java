package com.ajaros.reservationsystem.users.controllers;

import com.ajaros.reservationsystem.users.dtos.UpdateUserInformationRequest;
import com.ajaros.reservationsystem.users.entities.User;
import com.ajaros.reservationsystem.users.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
  private final UserService userService;

  @PatchMapping
  public ResponseEntity<Void> updateUserInformation(
      @Valid @RequestBody UpdateUserInformationRequest request,
      @AuthenticationPrincipal User user) {
    userService.updateUserInformation(user, request);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public User getUserInformation(@AuthenticationPrincipal User user) {
    return user;
  }

  @PutMapping("/password")
  public void updateUserPassword() {}
}
