package com.example.weblibrary.model.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Standard error response format.
 *
 * @param message General error message
 * @param timestamp Time when error occurred
 * @param errors Map of field errors (field name -> error message)
 */
public record ErrorResponse(
    String message,
    LocalDateTime timestamp,
    Map<String, String> errors
) {

  // Конструктор с неизменяемой копией errors для безопасности
  public ErrorResponse {
    errors = (errors != null) ? Collections.unmodifiableMap(new HashMap<>(errors)) : Collections.emptyMap();
  }

  // Вспомогательный метод для создания ErrorResponse с одним полем ошибки
  public static ErrorResponse of(String message, LocalDateTime timestamp, String field, String error) {
    Map<String, String> errors = new HashMap<>();
    errors.put(field, error);
    return new ErrorResponse(message, timestamp, errors);
  }

  // Метод для добавления новой ошибки (возвращает новый объект, так как record неизменяемый)
  public ErrorResponse withError(String field, String errorMessage) {
    Map<String, String> newErrors = new HashMap<>(errors);
    newErrors.put(field, errorMessage);
    return new ErrorResponse(message, timestamp, newErrors);
  }

  // Метод для проверки, пустой ли список ошибок
  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  // Метод для получения количества ошибок
  public int errorCount() {
    return errors.size();
  }
}