package com.example.weblibrary.service.impl;

import com.example.weblibrary.model.User;
import com.example.weblibrary.repository.UserRepository;
import com.example.weblibrary.service.CrudService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing users.
 * Provides methods to retrieve, create, update, and delete users.
 */
@Service
public class UserServiceImpl implements CrudService<User> {

  private final UserRepository userRepository;

  /**
   * Constructor for dependency injection.
   *
   * @param userRepository The UserRepository to be injected.
   */
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Retrieves all users from the database.
   *
   * @return A list of all users.
   */
  @Override
  public List<User> getAll() {
    return userRepository.findAll();  // Возвращаем всех пользователей
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param id The ID of the user to be retrieved.
   * @return An Optional containing the user if found, otherwise an empty Optional.
   */
  @Override
  public Optional<User> getById(int id) {
    return userRepository.findById(id);  // Ищем пользователя по id
  }

  /**
   * Creates a new user in the database.
   *
   * @param user The user to be created.
   * @return The created user.
   */
  @Override
  public User create(User user) {
    return userRepository.save(user);  // Сохраняем нового пользователя
  }

  /**
   * Updates an existing user by their ID.
   *
   * @param id   The ID of the user to be updated.
   * @param user The user data to update.
   * @return The updated user.
   * @throws RuntimeException if the user is not found.
   */
  @Override
  public User update(int id, User user) {
    if (userRepository.existsById(id)) {  // Проверяем, существует ли пользователь с таким id
      user.setId(id);  // Устанавливаем id пользователя
      return userRepository.save(user);  // Обновляем пользователя
    } else {
      throw new RuntimeException("User not found with id: " + id);  // Если пользователь не найден
    }
  }

  /**
   * Deletes a user by their ID.
   *
   * @param id The ID of the user to be deleted.
   * @throws RuntimeException if the user is not found.
   */
  @Override
  public void delete(int id) {
    if (userRepository.existsById(id)) {  // Проверяем, существует ли пользователь с таким id
      userRepository.deleteById(id);  // Удаляем пользователя
    } else {
      throw new RuntimeException("User not found with id: " + id);  // Если пользователь не найден
    }
  }
}
