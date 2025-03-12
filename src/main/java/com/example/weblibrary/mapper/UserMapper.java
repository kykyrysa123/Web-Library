package com.example.weblibrary.mapper;

import com.example.weblibrary.model.User;
import com.example.weblibrary.model.dto.UserDtoRequest;
import com.example.weblibrary.model.dto.UserDtoResponse;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between User entities and DTOs. Provides
 * methods to map User DTO requests to entities and entities to DTO responses.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

  /**
   * Converts a UserDtoRequest to a User entity.
   *
   * @param userDtoRequest The DTO object to be converted.
   * @return The corresponding User entity.
   */
  User toUserEntity(UserDtoRequest userDtoRequest);

  /**
   * Converts a list of UserDtoRequest objects to a list of User entities.
   *
   * @param userDtoRequest The list of DTO objects to be converted.
   * @return The corresponding list of User entities.
   */
  List<User> toUserEntity(List<UserDtoRequest> userDtoRequest);

  /**
   * Converts a User entity to a UserDtoResponse.
   *
   * @param user The User entity to be converted.
   * @return The corresponding UserDtoResponse.
   */
  UserDtoResponse toUserDtoResponse(User user);

  /**
   * Converts a list of User entities to a list of UserDtoResponse objects.
   *
   * @param user The list of User entities to be converted.
   * @return The corresponding list of UserDtoResponse objects.
   */
  List<UserDtoResponse> toUserDtoResponse(List<User> user);
}
