package com.example.weblibrary.exception;

import com.example.weblibrary.model.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(
      MethodArgumentNotValidException ex
  ) {
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


  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleInvalidIdFormat(
      MethodArgumentTypeMismatchException ex
  ) {
    log.warn("Invalid ID format: {}", ex.getMessage());

    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("id", "Некорректный формат ID. Ожидается число.");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorResponse("Ошибка валидации",
            LocalDateTime.now(ZoneId.systemDefault()), errorDetails));
  }


  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFound(
      EntityNotFoundException ex
  ) {
    String message = ex.getMessage()
        != null ? ex.getMessage() : "Запрашиваемый ресурс не найден";
    log.warn("Entity not found: {}", message);

    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("id", message);

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new ErrorResponse("Ресурс не найден",
            LocalDateTime.now(ZoneId.systemDefault()), errorDetails));
  }


  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(
      NoHandlerFoundException ex
  ) {
    log.warn("Invalid endpoint accessed: {}", ex.getRequestURL());

    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("endpoint", "Запрошенный ресурс не найден.");

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new ErrorResponse("Ресурс не найден",
            LocalDateTime.now(ZoneId.systemDefault()), errorDetails));
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
    log.error("Unexpected error occurred", ex);

    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("server",
        "Произошла непредвиденная ошибка. Проверьте корректность вашего ввода"
            + ".");

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new ErrorResponse("Внутренняя ошибка сервера",
            LocalDateTime.now(ZoneId.systemDefault()), errorDetails));
  }

  @ExceptionHandler(InvalidDateFormatException.class)
  public ResponseEntity<ErrorResponse> handleInvalidDateFormat(
      InvalidDateFormatException ex
  ) {
    log.warn("Invalid date format: {}", ex.getMessage());

    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("date", ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorResponse("Validation failed",
            LocalDateTime.now(ZoneId.systemDefault()), errorDetails));
  }

  @ExceptionHandler(LogsNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleLogsNotFound(
      LogsNotFoundException ex
  ) {
    log.warn("Logs not found: {}", ex.getMessage());

    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("date", ex.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new ErrorResponse("Validation failed",
            LocalDateTime.now(ZoneId.systemDefault()), errorDetails));
  }

}
