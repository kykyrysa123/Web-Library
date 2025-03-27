package com.example.weblibrary.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WebLibraryTest {
  // Пример зависимости для тестирования
  @Test
  void contextLoads() {
    // Test verifies that the Spring context loads without issues.
  }
}
