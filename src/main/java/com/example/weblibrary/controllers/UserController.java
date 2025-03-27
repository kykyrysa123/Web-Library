package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.UserDtoRequest;
import com.example.weblibrary.model.dto.UserDtoResponse;
import com.example.weblibrary.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing users.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Управление пользователями")
public class UserController {

  private final UserServiceImpl userService;

  @GetMapping
  @Operation(summary = "Получить всех пользователей", description =
      "Возвращает список всех пользователей")
  public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить пользователя по ID", description =
      "Возвращает пользователя по его идентификатору")
  public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getById(id));
  }

  @PostMapping
  @Operation(summary = "Создать пользователя", description = "Создаёт нового "
      + "пользователя и возвращает его данные")
  public ResponseEntity<UserDtoResponse> createUser(
      @Valid @RequestBody UserDtoRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(
        userService.create(request));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить данные пользователя", description =
      "Обновляет информацию о пользователе по его идентификатору")
  public ResponseEntity<UserDtoResponse> updateUser(@PathVariable Long id,
      @Valid @RequestBody UserDtoRequest request
  ) {
    return ResponseEntity.ok(userService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить пользователя", description = "Удаляет "
      + "пользователя по его идентификатору")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
