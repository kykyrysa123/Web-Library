package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.UserDtoRequest;
import com.example.weblibrary.model.dto.UserDtoResponse;
import com.example.weblibrary.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Управление пользователями")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')") // Все методы доступны только ADMIN
public class UserController {

  private final UserServiceImpl userService;

  @GetMapping
  @Operation(summary = "Получить всех пользователей")
  public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить пользователя по ID")
  public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getById(id));
  }

  @PostMapping
  @Operation(summary = "Создать пользователя")
  public ResponseEntity<UserDtoResponse> createUser(@Valid @RequestBody UserDtoRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить данные пользователя")
  public ResponseEntity<UserDtoResponse> updateUser(@PathVariable Long id,
                                                    @Valid @RequestBody UserDtoRequest request) {
    return ResponseEntity.ok(userService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить пользователя")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}