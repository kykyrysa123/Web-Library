package com.example.weblibrary.service.impl;

import com.example.weblibrary.mapper.UserMapperImpl;
import com.example.weblibrary.model.User;
import com.example.weblibrary.model.dto.UserDtoRequest;
import com.example.weblibrary.model.dto.UserDtoResponse;
import com.example.weblibrary.repository.UserRepository;
import com.example.weblibrary.service.CrudService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link CrudService} interface for {@link User} entities.
 * Provides methods for creating, reading, updating, and deleting users.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements CrudService<UserDtoRequest, UserDtoResponse> {

  private final UserRepository userRepository;
  private final UserMapperImpl userMapper;

  /**
   * Retrieves all users.
   *
   * @return List of {@link UserDtoResponse} objects.
   */
  @Override
  public List<UserDtoResponse> getAll() {
    return userMapper.toUserDtoResponse(userRepository.findAll());
  }

  /**
   * Retrieves a user by ID.
   *
   * @param id The ID of the user to retrieve.
   * @return The {@link UserDtoResponse} corresponding to the given ID.
   */
  @Override
  public UserDtoResponse getById(Long id) {
    return userMapper.toUserDtoResponse(
        userRepository.findById(id).orElseThrow(NullPointerException::new));
  }

  /**
   * Creates a new user.
   *
   * @param userDtoRequest The user data for creation.
   * @return The {@link UserDtoResponse} of the created user.
   */
  @Override
  public UserDtoResponse create(UserDtoRequest userDtoRequest) {
    return userMapper.toUserDtoResponse(
        userRepository.save(userMapper.toUserEntity(userDtoRequest)));
  }

  /**
   * Updates an existing user.
   *
   * @param id              The ID of the user to update.
   * @param userDtoRequest The new user data.
   * @return The updated {@link UserDtoResponse}.
   */
  @Override
  public UserDtoResponse update(Long id, UserDtoRequest userDtoRequest) {
    userRepository.findById(id).orElseThrow(
        () -> new RuntimeException("User not found with id: " + id));
    User updatedUser = userMapper.toUserEntity(userDtoRequest);
    return userMapper.toUserDtoResponse(userRepository.save(updatedUser));
  }

  /**
   * Deletes a user by ID.
   *
   * @param id The ID of the user to delete.
   */
  @Override
  public void delete(Long id) {
    User user = userRepository.findById(id).orElseThrow(
        () -> new RuntimeException("User not found with id: " + id));
    userRepository.delete(user);
  }

}
