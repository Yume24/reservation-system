package com.ajaros.reservationsystem.users.services;

import com.ajaros.reservationsystem.users.dtos.UpdateUserInformationRequest;
import com.ajaros.reservationsystem.users.entities.User;
import com.ajaros.reservationsystem.users.exceptions.PasswordChangeException;
import com.ajaros.reservationsystem.users.exceptions.UserNotFoundException;
import com.ajaros.reservationsystem.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
  }

  @Transactional
  public void updateUserInformation(long userId, UpdateUserInformationRequest request) {
    var user = getUserById(userId);
    user.setName(request.name());
    user.setSurname(request.surname());
  }

  @Transactional
  public void updateUserPassword(long userId, String oldPassword, String newPassword) {
    var user = getUserById(userId);
    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new PasswordChangeException("Old password is incorrect");
    }
    user.setPassword(passwordEncoder.encode(newPassword));
  }

  public User getUserById(long userId) {
    return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
  }
}
