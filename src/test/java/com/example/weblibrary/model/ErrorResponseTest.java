package com.example.weblibrary.model;

import com.example.weblibrary.model.dto.ErrorResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

  @Test
  void testFullConstructor() {
    Map<String, String> errors = new HashMap<>();
    errors.put("field1", "error1");
    LocalDateTime timestamp = LocalDateTime.now();
    ErrorResponse response = new ErrorResponse("General error", timestamp, errors);

    assertEquals("General error", response.message());
    assertEquals(timestamp, response.timestamp());
    assertEquals(1, response.errors().size());
    assertEquals("error1", response.errors().get("field1"));
    assertThrows(UnsupportedOperationException.class, () -> response.errors().put("field2", "error2")); // Проверка неизменяемости
  }

  @Test
  void testNullErrors() {
    LocalDateTime timestamp = LocalDateTime.now();
    ErrorResponse response = new ErrorResponse("General error", timestamp, null);

    assertEquals("General error", response.message());
    assertEquals(timestamp, response.timestamp());
    assertTrue(response.errors().isEmpty());
  }

  @Test
  void testOfMethod() {
    LocalDateTime timestamp = LocalDateTime.now();
    ErrorResponse response = ErrorResponse.of("General error", timestamp, "field1", "error1");

    assertEquals("General error", response.message());
    assertEquals(timestamp, response.timestamp());
    assertEquals(1, response.errors().size());
    assertEquals("error1", response.errors().get("field1"));
  }

  @Test
  void testWithError() {
    LocalDateTime timestamp = LocalDateTime.now();
    ErrorResponse original = new ErrorResponse("General error", timestamp, Map.of("field1", "error1"));
    ErrorResponse updated = original.withError("field2", "error2");

    assertEquals(1, original.errors().size());
    assertEquals(2, updated.errors().size());
    assertEquals("error1", updated.errors().get("field1"));
    assertEquals("error2", updated.errors().get("field2"));
  }

  @Test
  void testHasErrors() {
    LocalDateTime timestamp = LocalDateTime.now();
    ErrorResponse withErrors = new ErrorResponse("Error", timestamp, Map.of("field1", "error1"));
    ErrorResponse noErrors = new ErrorResponse("Error", timestamp, null);

    assertTrue(withErrors.hasErrors());
    assertFalse(noErrors.hasErrors());
  }

  @Test
  void testErrorCount() {
    LocalDateTime timestamp = LocalDateTime.now();
    ErrorResponse response = new ErrorResponse("Error", timestamp, Map.of("field1", "error1", "field2", "error2"));

    assertEquals(2, response.errorCount());
  }
}