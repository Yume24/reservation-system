package com.ajaros.reservationsystem.users.dtos;

import com.ajaros.reservationsystem.users.entities.Role;

public record UserInformation(String email, String name, String surname, Role role) {}
