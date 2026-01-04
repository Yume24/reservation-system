package com.ajaros.reservationsystem.auth.controllers;

import com.ajaros.reservationsystem.auth.dtos.RegisterRequest;
import com.ajaros.reservationsystem.auth.exceptions.UserAlreadyExistsException;
import com.ajaros.reservationsystem.auth.services.RegistrationService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
  private RegistrationService registrationService;

  @PostMapping("/register")
  public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
    registrationService.registerUser(registerRequest);
    return ResponseEntity.status(201).build();
  }

  @PostMapping("/login")
  public ResponseEntity<?> login() {
    return null;
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout() {
    return null;
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleUserAlreadyExists(
      UserAlreadyExistsException ex) {
    return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
  }
}
