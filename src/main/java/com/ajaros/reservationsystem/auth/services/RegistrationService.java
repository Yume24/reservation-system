package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.auth.dtos.RegisterRequest;
import com.ajaros.reservationsystem.auth.dtos.RegisterResponse;
import com.ajaros.reservationsystem.auth.exceptions.UserAlreadyExistsException;
import com.ajaros.reservationsystem.users.entities.Role;
import com.ajaros.reservationsystem.users.entities.User;
import com.ajaros.reservationsystem.users.mappers.UserMapper;
import com.ajaros.reservationsystem.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public RegisterResponse registerUser(RegisterRequest request) {
    var email = request.email();
    var name = request.name();
    var surname = request.surname();
    var password = request.password();

    var user = userRepository.findByEmail(email).orElse(null);
    if (user != null) throw new UserAlreadyExistsException(email);

    user =
        User.builder()
            .email(email)
            .name(name)
            .surname(surname)
            .password(passwordEncoder.encode(password))
            .role(Role.USER)
            .build();

    userRepository.save(user);

    return userMapper.toRegisterResponse(user);
  }
}
