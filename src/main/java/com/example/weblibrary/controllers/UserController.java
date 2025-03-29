package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.UserDtoRequest;
import com.example.weblibrary.model.dto.UserDtoResponse;
import com.example.weblibrary.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing users.
 * Provides CRUD operations for user management via REST API.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Управление пользователями")
public class UserController {

  private final UserServiceImpl userService;

  /**
   * Retrieves all users.
   *
   * @return ResponseEntity containing list of all users
   */
  @GetMapping
  @Operation(summary = "Получить всех пользователей",
      description = "Возвращает список всех пользователей")
  public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAll());
  }

  /**
   * Retrieves a user by ID.
   *
   * @param id the ID of the user to retrieve
   * @return ResponseEntity containing the user data
   */
  @GetMapping("/{id}")
  @Operation(summary = "Получить пользователя по ID",
      description = "Возвращает пользователя по его идентификатору")
  public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getById(id));
  }

  /**
   * Creates a new user.
   *
   * @param request DTO containing user data
   * @return ResponseEntity containing the created user
   */
  @PostMapping
  @Operation(summary = "Создать пользователя",
      description = "Создаёт нового пользователя и возвращает его данные")
  public ResponseEntity<UserDtoResponse> createUser(
      @Valid @RequestBody UserDtoRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(userService.create(request));
  }

  /**
   * Updates an existing user.
   *
   * @param id the ID of the user to update
   * @param request DTO containing updated user data
   * @return ResponseEntity containing the updated user
   */
  @PutMapping("/{id}")
  @Operation(summary = "Обновить данные пользователя",
      description = "Обновляет информацию о пользователе по его идентификатору")
  public ResponseEntity<UserDtoResponse> updateUser(
      @PathVariable Long id,
      @Valid @RequestBody UserDtoRequest request) {
    return ResponseEntity.ok(userService.update(id, request));
  }

  /**
   * Deletes a user.
   *
   * @param id the ID of the user to delete
   * @return ResponseEntity with no content
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить пользователя",
      description = "Удаляет пользователя по его идентификатору")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}