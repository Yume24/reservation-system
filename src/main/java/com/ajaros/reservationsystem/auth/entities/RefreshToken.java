package com.ajaros.reservationsystem.auth.entities;

import com.ajaros.reservationsystem.users.entities.User;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "Refresh_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "token")
  private String token;

  @Column(name = "creation_date")
  private Instant creationDate;

  @Column(name = "expiration_date")
  private Instant expirationDate;

  @JoinColumn(name = "user")
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;
}
