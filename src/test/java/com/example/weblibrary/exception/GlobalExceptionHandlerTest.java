package com.example.weblibrary.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.weblibrary.model.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GlobalExceptionHandler();
  }

  @Test
  void testHandleValidationErrors() {
    MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
    BindingResult bindingResult = mock(BindingResult.class);
    FieldError fieldError = new FieldError("objectName", "fieldName", "Invalid value");
    when(ex.getBindingResult()).thenReturn(bindingResult);
    when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

    ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex);

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    ErrorResponse errorResponse = response.getBody();
    assertNotNull(errorResponse);
    assertEquals("Ошибка валидации", errorResponse.message());
    assertNotNull(errorResponse.timestamp());
    Map<String, String> details = errorResponse.errors();
    assertEquals(1, details.size());
    assertEquals("Invalid value", details.get("fieldName"));
  }

  @Test
  void testHandleInvalidIdFormat() {
    MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
    when(ex.getMessage()).thenReturn("Failed to convert value");

    ResponseEntity<ErrorResponse> response = handler.handleInvalidIdFormat(ex);

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    ErrorResponse errorResponse = response.getBody();
    assertNotNull(errorResponse);
    assertEquals("Ошибка валидации", errorResponse.message());
    assertNotNull(errorResponse.timestamp());
    Map<String, String> details = errorResponse.errors();
    assertEquals(1, details.size());
    assertEquals("Некорректный формат ID. Ожидается число.", details.get("id"));
  }

  @Test
  void testHandleEntityNotFound_WithMessage() {
    EntityNotFoundException ex = new EntityNotFoundException("Author not found");

    ResponseEntity<ErrorResponse> response = handler.handleEntityNotFound(ex);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    ErrorResponse errorResponse = response.getBody();
    assertNotNull(errorResponse);
    assertEquals("Ресурс не найден", errorResponse.message());
    assertNotNull(errorResponse.timestamp());
    Map<String, String> details = errorResponse.errors();
    assertEquals(1, details.size());
    assertEquals("Author not found", details.get("id"));
  }

  @Test
  void testHandleEntityNotFound_NoMessage() {
    EntityNotFoundException ex = new EntityNotFoundException();

    ResponseEntity<ErrorResponse> response = handler.handleEntityNotFound(ex);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    ErrorResponse errorResponse = response.getBody();
    assertNotNull(errorResponse);
    assertEquals("Ресурс не найден", errorResponse.message());
    assertNotNull(errorResponse.timestamp());
    Map<String, String> details = errorResponse.errors();
    assertEquals(1, details.size());
    assertEquals("Запрашиваемый ресурс не найден", details.get("id"));
  }

  @Test
  void testHandleNotFound() {
    NoHandlerFoundException ex = mock(NoHandlerFoundException.class);
    when(ex.getRequestURL()).thenReturn("/invalid/path");

    ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    ErrorResponse errorResponse = response.getBody();
    assertNotNull(errorResponse);
    assertEquals("Ресурс не найден", errorResponse.message());
    assertNotNull(errorResponse.timestamp());
    Map<String, String> details = errorResponse.errors();
    assertEquals(1, details.size());
    assertEquals("Запрошенный ресурс не найден.", details.get("endpoint"));
  }

  @Test
  void testHandleAllExceptions() {
    Exception ex = new RuntimeException("Unexpected error");

    ResponseEntity<ErrorResponse> response = handler.handleAllExceptions(ex);

    assertNotNull(response);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    ErrorResponse errorResponse = response.getBody();
    assertNotNull(errorResponse);
    assertEquals("Внутренняя ошибка сервера", errorResponse.message());
    assertNotNull(errorResponse.timestamp());
    Map<String, String> details = errorResponse.errors();
    assertEquals(1, details.size());
    assertEquals("Произошла непредвиденная ошибка. Проверьте корректность вашего ввода.", details.get("server"));
  }

  @Test
  void testHandleInvalidDateFormat() {
    InvalidDateFormatException ex = new InvalidDateFormatException("Invalid date format");

    ResponseEntity<ErrorResponse> response = handler.handleInvalidDateFormat(ex);

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    ErrorResponse errorResponse = response.getBody();
    assertNotNull(errorResponse);
    assertEquals("Validation failed", errorResponse.message());
    assertNotNull(errorResponse.timestamp());
    Map<String, String> details = errorResponse.errors();
    assertEquals(1, details.size());
    assertEquals("Invalid date format", details.get("date"));
  }

  @Test
  void testHandleLogsNotFound() {
    LogsNotFoundException ex = new LogsNotFoundException("Logs not found for date");

    ResponseEntity<ErrorResponse> response = handler.handleLogsNotFound(ex);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    ErrorResponse errorResponse = response.getBody();
    assertNotNull(errorResponse);
    assertEquals("Validation failed", errorResponse.message());
    assertNotNull(errorResponse.timestamp());
    Map<String, String> details = errorResponse.errors();
    assertEquals(1, details.size());
    assertEquals("Logs not found for date", details.get("date"));
  }
}