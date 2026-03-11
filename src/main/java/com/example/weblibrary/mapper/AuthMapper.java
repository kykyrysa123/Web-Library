package com.example.weblibrary.mapper;

import com.example.weblibrary.model.User;
import com.example.weblibrary.model.dto.AuthResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

  @Mapping(target = "token", source = "token")
  @Mapping(target = "refreshToken", source = "refreshToken")
  @Mapping(target = "username", source = "user.username")
  AuthResponse toAuthResponse(String token, String refreshToken, User user);

}
