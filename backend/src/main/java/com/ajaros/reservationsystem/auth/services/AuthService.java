package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.auth.dtos.LoginRequest;
import com.ajaros.reservationsystem.auth.dtos.RegisterRequest;
import com.ajaros.reservationsystem.auth.dtos.RegisterResponse;
import com.ajaros.reservationsystem.auth.exceptions.UserAlreadyExistsException;
import com.ajaros.reservationsystem.auth.dtos.AuthTokensInfo;
import com.ajaros.reservationsystem.users.entities.Role;
import com.ajaros.reservationsystem.users.entities.User;
import com.ajaros.reservationsystem.users.mappers.UserMapper;
import com.ajaros.reservationsystem.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserMapper userMapper;
  private final RefreshTokenService refreshTokenService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthTokensInfo login(LoginRequest loginRequest) {
    var auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

    var user = (User) auth.getPrincipal();
    var accessToken = jwtService.generateAccessToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    refreshTokenService.saveRefreshToken(refreshToken, user);

    return new AuthTokensInfo(accessToken, refreshToken, userMapper.toUserInformation(user));
  }

  public RegisterResponse registerUser(RegisterRequest request) {
    var user =
        User.builder()
            .email(request.email())
            .name(request.name())
            .surname(request.surname())
            .password(passwordEncoder.encode(request.password()))
            .role(Role.USER)
            .build();

    try {
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new UserAlreadyExistsException(request.email());
    }
    return userMapper.toRegisterResponse(user);
  }
}
