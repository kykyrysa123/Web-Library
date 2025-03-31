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

  /**
   * Compact constructor that creates an unmodifiable copy of the errors map.
   */
  public ErrorResponse {
    errors = (errors != null)
        ? Collections.unmodifiableMap(new HashMap<>(errors))
        : Collections.emptyMap();
  }

  /**
   * Creates an ErrorResponse with a single field error.
   *
   * @param message General error message
   * @param timestamp Time when error occurred
   * @param field The field name with error
   * @param error The error message for the field
   * @return New ErrorResponse instance
   */
  public static ErrorResponse of(String message, LocalDateTime timestamp,
      String field, String error) {
    Map<String, String> errors = new HashMap<>();
    errors.put(field, error);
    return new ErrorResponse(message, timestamp, errors);
  }

  /**
   * Adds a new field error to the response.
   *
   * @param field The field name to add
   * @param errorMessage The error message for the field
   * @return New ErrorResponse instance with additional error
   */
  public ErrorResponse withError(String field, String errorMessage) {
    Map<String, String> newErrors = new HashMap<>(errors);
    newErrors.put(field, errorMessage);
    return new ErrorResponse(message, timestamp, newErrors);
  }

  /**
   * Checks if the response contains any field errors.
   *
   * @return true if errors exist, false otherwise
   */
  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  /**
   * Returns the number of field errors.
   *
   * @return count of field errors
   */
  public int errorCount() {
    return errors.size();
  }
}