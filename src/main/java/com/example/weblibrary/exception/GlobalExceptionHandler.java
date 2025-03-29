package com.example.weblibrary.exception;

import com.example.weblibrary.model.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Global exception handler for REST controllers.
 * Provides centralized exception handling across all controllers and returns
 * standardized error responses.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles validation errors from @Valid annotated parameters.
   *
   * @param ex the validation exception
   * @return ResponseEntity containing validation error details
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    log.warn("Validation errors: {}", errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorResponse("Ошибка валидации",
            LocalDateTime.now(ZoneId.systemDefault()), errors));
  }

  /**
   * Handles type mismatch for method arguments.
   *
   * @param ex the type mismatch exception
   * @return ResponseEntity containing error details
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleInvalidIdFormat(
      MethodArgumentTypeMismatchException ex) {
    log.warn("Invalid ID format: {}", ex.getMessage());

    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("id", "Некорректный формат ID. Ожидается число.");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorResponse("Ошибка валидации",
            LocalDateTime.now(ZoneId.systemDefault()), errorDetails));
  }

  /**
   * Handles entity not found exceptions.
   *
   * @param ex the not found exception
   * @return ResponseEntity containing error details
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFound(
      EntityNotFoundException ex) {
    String message = ex.getMessage() != null
        ? ex.getMessage() : "Запрашиваемый ресурс не найден";
    log.warn("Entity not found: {}", message);

    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("id", message);

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new ErrorResponse("Ресурс не найден",
            LocalDateTime.now(ZoneId.systemDefault()), errorDetails));
  }

  /**
   * Handles requests to non-existent endpoints.
   *
   * @param ex the no handler found exception
   * @return ResponseEntity containing error details
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(
      NoHandlerFoundException ex) {
    log.warn("Invalid endpoint accessed: {}", ex.getRequestURL());

    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("endpoint", "Запрошенный ресурс не найден.");

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new ErrorResponse("Ресурс не найден",
            LocalDateTime.now(ZoneId.systemDefault()), errorDetails));
  }

  /**
   * Handles all other unexpected exceptions.
   *
   * @param ex the caught exception
   * @return ResponseEntity containing error details
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
    log.error("Unexpected error occurred", ex);

    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("server",
        "Произошла непредвиденная ошибка. Проверьте корректность вашего ввода.");

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new ErrorResponse("Внутренняя ошибка сервера",
            LocalDateTime.now(ZoneId.systemDefault()), errorDetails));
  }

  /**
   * Handles invalid date format exceptions.
   *
   * @param ex the invalid date format exception
   * @return ResponseEntity containing error details
   */
  @ExceptionHandler(InvalidDateFormatException.class)
  public ResponseEntity<ErrorResponse> handleInvalidDateFormat(
      InvalidDateFormatException ex) {
    log.warn("Invalid date format: {}", ex.getMessage());

    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("date", ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorResponse("Validation failed",
            LocalDateTime.now(ZoneId.systemDefault()), errorDetails));
  }

  /**
   * Handles logs not found exceptions.
   *
   * @param ex the logs not found exception
   * @return ResponseEntity containing error details
   */
  @ExceptionHandler(LogsNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleLogsNotFound(
      LogsNotFoundException ex) {
    log.warn("Logs not found: {}", ex.getMessage());

    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("date", ex.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new ErrorResponse("Validation failed",
            LocalDateTime.now(ZoneId.systemDefault()), errorDetails));
  }
}