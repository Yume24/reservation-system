package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.auth.dtos.LoginRequest;
import com.ajaros.reservationsystem.users.entities.User;
import com.ajaros.reservationsystem.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;

  public String login(LoginRequest loginRequest) {
    var auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

    var user = (User) auth.getPrincipal();
    return jwtService.generateAccessToken(user);
  }
}
