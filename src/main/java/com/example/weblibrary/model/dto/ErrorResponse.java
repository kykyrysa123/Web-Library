package com.example.weblibrary.model.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response format
 * @param message General error message
 * @param timestamp Time when error occurred
 * @param errors Map of field errors (field name -> error message)
 */
public record ErrorResponse(
    String message,
    LocalDateTime timestamp,
    Map<String, String> errors
) {
  public ErrorResponse(String message, Map<String, String> errors) {
    this(message, LocalDateTime.now(), errors);
  }
}