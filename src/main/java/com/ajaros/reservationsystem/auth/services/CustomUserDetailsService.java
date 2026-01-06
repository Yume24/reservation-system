package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.users.entities.User;
import com.ajaros.reservationsystem.users.repositories.UserRepository;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  @Nonnull
  public User loadUserByUsername(@Nonnull String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User with this email does not exist"));
  }
}
