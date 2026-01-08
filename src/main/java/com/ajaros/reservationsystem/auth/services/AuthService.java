package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.auth.dtos.LoginRequest;
import com.ajaros.reservationsystem.auth.dtos.RegisterRequest;
import com.ajaros.reservationsystem.auth.dtos.RegisterResponse;
import com.ajaros.reservationsystem.auth.exceptions.UserAlreadyExistsException;
import com.ajaros.reservationsystem.auth.utils.AuthTokensInfo;
import com.ajaros.reservationsystem.users.entities.Role;
import com.ajaros.reservationsystem.users.entities.User;
import com.ajaros.reservationsystem.users.mappers.UserMapper;
import com.ajaros.reservationsystem.users.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final RefreshTokenService refreshTokenService;

  public RegisterResponse registerUser(RegisterRequest request) {
    var email = request.email();
    var name = request.name();
    var surname = request.surname();
    var password = request.password();

    var user =
        User.builder()
            .email(email)
            .name(name)
            .surname(surname)
            .password(passwordEncoder.encode(password))
            .role(Role.USER)
            .build();

    try {
      userService.saveUser(user);
    } catch (DataIntegrityViolationException e) {
      throw new UserAlreadyExistsException(email);
    }
    return userMapper.toRegisterResponse(user);
  }

  public AuthTokensInfo login(LoginRequest loginRequest) {
    var auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

    var user = (User) auth.getPrincipal();
    if (user == null)
      throw new UsernameNotFoundException("User not found with email: " + loginRequest.email());
    var accessToken = jwtService.generateAccessToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    refreshTokenService.saveRefreshToken(refreshToken, user);

    return new AuthTokensInfo(accessToken, refreshToken, user);
  }
}
