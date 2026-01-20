package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.users.entities.User;
import com.ajaros.reservationsystem.users.exceptions.UserNotFoundException;
import com.ajaros.reservationsystem.users.services.UserService;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserService userService;

  @Override
  @Nonnull
  public User loadUserByUsername(@Nonnull String email) {
    try {
      return userService.getUserByEmail(email);
    } catch (UserNotFoundException _) {
      throw new UsernameNotFoundException("User with this email does not exist");
    }
  }
}
