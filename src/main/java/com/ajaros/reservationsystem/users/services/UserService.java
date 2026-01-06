package com.ajaros.reservationsystem.users.services;

import com.ajaros.reservationsystem.users.dtos.UpdateUserInformationRequest;
import com.ajaros.reservationsystem.users.entities.User;
import com.ajaros.reservationsystem.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public void updateUserInformation(User user, UpdateUserInformationRequest request) {
    user.setName(request.name());
    user.setSurname(request.surname());

    userRepository.save(user);
  }
}
