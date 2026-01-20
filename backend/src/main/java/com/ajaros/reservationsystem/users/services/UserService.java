package com.ajaros.reservationsystem.users.services;

import com.ajaros.reservationsystem.auth.dtos.RegisterRequest;
import com.ajaros.reservationsystem.auth.dtos.RegisterResponse;
import com.ajaros.reservationsystem.auth.exceptions.UserAlreadyExistsException;
import com.ajaros.reservationsystem.users.dtos.UpdateUserInformationRequest;
import com.ajaros.reservationsystem.users.entities.Role;
import com.ajaros.reservationsystem.users.entities.User;
import com.ajaros.reservationsystem.users.exceptions.PasswordChangeException;
import com.ajaros.reservationsystem.users.exceptions.UserNotFoundException;
import com.ajaros.reservationsystem.users.mappers.UserMapper;
import com.ajaros.reservationsystem.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
  }

  @Transactional
  public void updateUserInformation(long userId, UpdateUserInformationRequest request) {
    var user = getUserById(userId);
    user.setName(request.name());
    user.setSurname(request.surname());
    userRepository.save(user);
  }

  @Transactional
  public void updateUserPassword(long userId, String oldPassword, String newPassword) {
    var user = getUserById(userId);

    if (!passwordEncoder.matches(oldPassword, user.getPassword()))
      throw new PasswordChangeException("Old password is incorrect");

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }

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
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new UserAlreadyExistsException(email);
    }
    return userMapper.toRegisterResponse(user);
  }

  public User getUserById(long userId) {
    return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
  }
}
