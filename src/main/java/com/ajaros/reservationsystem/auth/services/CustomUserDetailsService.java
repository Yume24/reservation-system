package com.ajaros.reservationsystem.auth.services;

import com.ajaros.reservationsystem.users.repositories.UserRepository;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
  private UserRepository userRepository;

  @Override
  @Nonnull
  public UserDetails loadUserByUsername(@Nonnull String email) throws UsernameNotFoundException {
    var user =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () -> new UsernameNotFoundException("User with this email does not exist"));
    var role = user.getRole();
    return User.withUsername(user.getEmail())
        .authorities(new SimpleGrantedAuthority("ROLE_" + role.toString()))
        .password(user.getPassword())
        .build();
  }
}
