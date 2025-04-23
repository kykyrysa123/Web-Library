package com.example.weblibrary.service.impl;

import com.example.weblibrary.mapper.UserMapperImpl;
import com.example.weblibrary.model.User;
import com.example.weblibrary.model.dto.UserDtoRequest;
import com.example.weblibrary.model.dto.UserDtoResponse;
import com.example.weblibrary.repository.UserRepository;
import com.example.weblibrary.service.CrudService;
import com.example.weblibrary.service.cache.SimpleCache;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link CrudService} interface for {@link User}
 * entities. Provides methods for creating, reading, updating, and deleting
 * users.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements CrudService<UserDtoRequest,
    UserDtoResponse> {

  private final UserRepository userRepository;
  private final UserMapperImpl userMapper;

  private final SimpleCache<Long, UserDtoResponse> userCache =
      new SimpleCache<>(
      1000);
  private final SimpleCache<String, List<UserDtoResponse>> userListCache =
      new SimpleCache<>(
      1000);
  private static final Logger log = LoggerFactory.getLogger(
      UserServiceImpl.class);

  /**
   * Retrieves all users.
   *
   * @return List of {@link UserDtoResponse} objects.
   */
  @Override
  public List<UserDtoResponse> getAll() {
    String cacheKey = "all_users";

    List<UserDtoResponse> cachedList = userListCache.get(cacheKey);
    if (cachedList != null) {
      log.info("Data for the user is obtained from the cache (getAll)");
      return cachedList;
    }

    log.info("Data for the user is loaded from the database (getAll)");
    List<UserDtoResponse> responseList = userMapper.toUserDtoResponse(
        userRepository.findAll());
    userListCache.put(cacheKey, responseList);
    return responseList;
  }

  /**
   * Retrieves a user by ID.
   *
   * @param id
   *     The ID of the user to retrieve.
   * @return The {@link UserDtoResponse} corresponding to the given ID.
   */
  @Override
  public UserDtoResponse getById(Long id) {
    UserDtoResponse cachedUser = userCache.get(id);
    if (cachedUser != null) {
      log.info("User with id={} retrieved from cache", id);
      return cachedUser;
    }

    log.info("User with id={} is loaded from the database", id);
    User user = userRepository.findById(id).orElseThrow(
        () -> new RuntimeException("User not found with id: " + id));

    UserDtoResponse response = userMapper.toUserDtoResponse(user);
    userCache.put(id, response);
    return response;
  }

  /**
   * Creates a new user.
   *
   * @param userDtoRequest
   *     The user data for creation.
   * @return The {@link UserDtoResponse} of the created user.
   */
  @Override
  public UserDtoResponse create(UserDtoRequest userDtoRequest) {
    User user = userMapper.toUserEntity(userDtoRequest);
    User savedUser = userRepository.save(user);
    UserDtoResponse response = userMapper.toUserDtoResponse(savedUser);

    userCache.put(savedUser.getId(), response);
    userListCache.clear(); // Очистка кэша списка пользователей

    return response;
  }

  /**
   * Updates an existing user.
   *
   * @param id
   *     The ID of the user to update.
   * @param userDtoRequest
   *     The new user data.
   * @return The updated {@link UserDtoResponse}.
   */
  @Override
  public UserDtoResponse update(Long id, UserDtoRequest userDtoRequest) {

    User updatedUser = userMapper.toUserEntity(userDtoRequest);
    updatedUser.setId(id);

    UserDtoResponse response = userMapper.toUserDtoResponse(
        userRepository.save(updatedUser));

    userCache.put(id, response);
    userListCache.clear(); // Очистка кэша списка пользователей

    return response;
  }

  /**
   * Deletes a user by ID.
   *
   * @param id
   *     The ID of the user to delete.
   */
  @Override
  public void delete(Long id) {
    User user = userRepository.findById(id).orElseThrow(
        () -> new RuntimeException("User not found with id: " + id));

    userRepository.delete(user);
    userCache.remove(id);
    userListCache.clear(); // Очистка кэша списка пользователей
  }
}
