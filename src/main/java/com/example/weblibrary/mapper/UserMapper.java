package com.example.weblibrary.mapper;

import com.example.weblibrary.model.User;
import com.example.weblibrary.model.dto.UserDtoRequest;
import com.example.weblibrary.model.dto.UserDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "id", ignore = true) // ID генерируется БД
  @Mapping(target = "username", source = "username")
  @Mapping(target = "password", source = "password")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "role", source = "role")
  @Mapping(target = "enabled", constant = "true")
  @Mapping(target = "registrationDate", expression = "java(java.time.LocalDate.now())")
  User toUserEntity(UserDtoRequest userDtoRequest);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "username", source = "username")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "role", source = "role")
  @Mapping(target = "registrationDate", source = "registrationDate")
  UserDtoResponse toUserDtoResponse(User user);

  List<UserDtoResponse> toUserDtoResponse(List<User> users);
}