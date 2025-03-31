package com.example.weblibrary.model;

import com.example.weblibrary.model.dto.LogResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogResponseTest {

  @Test
  void testDefaultConstructorAndSetters() {
    LogResponse response = new LogResponse();
    response.setDate("2023-10-01");
    response.setLogContent("Log content");

    assertEquals("2023-10-01", response.getDate());
    assertEquals("Log content", response.getLogContent());
  }

  @Test
  void testGettersWithNullValues() {
    LogResponse response = new LogResponse();

    assertNull(response.getDate());
    assertNull(response.getLogContent());
  }
}