package com.ajaros.reservationsystem.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ajaros.reservationsystem.repositories.UserRepository;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with this email does not exist"));
        var role = user.getRole().getRoleName();
        return User
                .withUsername(user.getEmail())
                .authorities(role)
                .password(user.getPassword())
                .build();
    }
}
