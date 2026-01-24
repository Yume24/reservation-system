package com.ajaros.reservationsystem.users.entities;

import com.ajaros.reservationsystem.auth.entities.RefreshToken;
import com.ajaros.reservationsystem.reservations.entities.Reservation;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import java.util.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "email")
  private String email;

  @Column(name = "name")
  private String name;

  @Column(name = "surname")
  private String surname;

  @Column(name = "password")
  private String password;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  @Builder.Default
  private Set<RefreshToken> refreshTokens = new HashSet<>();

  @OneToMany(mappedBy = "user")
  @Builder.Default
  private Set<Reservation> reservations = new HashSet<>();

  @Override
  @Nonnull
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  @Nonnull
  public String getUsername() {
    return email;
  }
}
