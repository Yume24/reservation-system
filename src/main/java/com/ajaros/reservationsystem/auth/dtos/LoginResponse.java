package com.ajaros.reservationsystem.auth.dtos;

import com.ajaros.reservationsystem.users.entities.Role;
import lombok.Builder;

@Builder
public record LoginResponse(String token, String name, String surname, String email, Role role) {}
