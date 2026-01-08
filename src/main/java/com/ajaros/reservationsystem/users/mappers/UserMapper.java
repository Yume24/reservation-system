package com.ajaros.reservationsystem.users.mappers;

import com.ajaros.reservationsystem.auth.dtos.LoginResponse;
import com.ajaros.reservationsystem.auth.dtos.RegisterResponse;
import com.ajaros.reservationsystem.users.dtos.UserInformation;
import com.ajaros.reservationsystem.users.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "accessToken", expression = "java(token)")
  LoginResponse toLoginResponse(User user, String token);

  RegisterResponse toRegisterResponse(User user);

  UserInformation toUserInformation(User user);
}
