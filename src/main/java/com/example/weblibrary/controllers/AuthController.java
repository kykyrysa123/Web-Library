package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.AuthResponse;
import com.example.weblibrary.model.dto.LoginRequest;
import com.example.weblibrary.model.dto.RegisterRequest;
import com.example.weblibrary.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API для аутентификации пользователей")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Вход в систему")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Успешный вход"),
          @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
  })
  public AuthResponse login(@Valid @RequestBody LoginRequest request) {
    return authService.login(request.username(), request.password());
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Регистрация нового пользователя")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
          @ApiResponse(responseCode = "400", description = "Некорректные данные или пользователь уже существует")
  })
  public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
    return authService.register(request.username(), request.password(), request.email());
  }

  @PostMapping("/refresh")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Обновление токена доступа")
  public AuthResponse refresh(@RequestHeader("Authorization") String authHeader) {
    return authService.refresh(authHeader);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Выход из системы")
  public void logout(@RequestHeader("Authorization") String authHeader) {
    authService.logout(authHeader);
  }
}