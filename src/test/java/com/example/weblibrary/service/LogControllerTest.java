package com.example.weblibrary.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.weblibrary.controllers.LogController;
import com.example.weblibrary.exception.InvalidDateFormatException;
import com.example.weblibrary.exception.LogProcessingException;
import com.example.weblibrary.exception.LogsNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class LogControllerTest {

  @InjectMocks
  private LogController logController;

  @Mock
  private Path logFile;

  @Mock
  private Path datedLogFile;

  @BeforeEach
  void setUp() {
    lenient().when(logFile.toString()).thenReturn("./test-logs/web-library.log");
    lenient().when(datedLogFile.toString()).thenReturn("./test-logs/log-2025-03-31.log");
    logController.setLogPath("./test-logs");
  }

  @Test
  void testGetLogsByDate_Success() throws IOException {
    try (MockedStatic<Paths> pathsMock = mockStatic(Paths.class);
         MockedStatic<Files> filesMock = mockStatic(Files.class)) {
      String dateString = "2025-03-31";
      List<String> logLines = List.of("2025-03-31 10:00:00 INFO Test log entry");
      byte[] fileContent = String.join("\n", logLines).getBytes();

      pathsMock.when(() -> Paths.get("./test-logs", "web-library.log")).thenReturn(logFile);
      pathsMock.when(() -> Paths.get("./test-logs", "log-2025-03-31.log")).thenReturn(datedLogFile);
      filesMock.when(() -> Files.exists(logFile)).thenReturn(true);
      filesMock.when(() -> Files.lines(logFile)).thenReturn(logLines.stream());
      filesMock.when(() -> Files.write(eq(datedLogFile), eq(logLines), any())).thenReturn(datedLogFile);
      filesMock.when(() -> Files.readAllBytes(datedLogFile)).thenReturn(fileContent);

      ResponseEntity<byte[]> response = logController.getLogsByDate(dateString);

      assertNotNull(response);
      assertEquals(200, response.getStatusCodeValue());
      assertArrayEquals(fileContent, response.getBody());
      assertTrue(response.getStatusCode().is2xxSuccessful());
    }
  }

  @Test
  void testGetLogsByDate_InvalidDateFormat() {
    try (MockedStatic<Paths> pathsMock = mockStatic(Paths.class);
         MockedStatic<Files> filesMock = mockStatic(Files.class)) {
      String invalidDateString = "2025-13-31";

      pathsMock.when(() -> Paths.get("./test-logs", "web-library.log")).thenReturn(logFile);

      InvalidDateFormatException exception = assertThrows(InvalidDateFormatException.class,
          () -> logController.getLogsByDate(invalidDateString));
      assertEquals("Дата указана в неверном формате. Ожидаемый формат: YYYY-MM-DD",
          exception.getMessage());
    }
  }

  @Test
  void testGetLogsByDate_FutureDate() {
    try (MockedStatic<Paths> pathsMock = mockStatic(Paths.class);
         MockedStatic<Files> filesMock = mockStatic(Files.class)) {
      String futureDateString = "2025-04-01";

      pathsMock.when(() -> Paths.get("./test-logs", "web-library.log")).thenReturn(logFile);

      InvalidDateFormatException exception = assertThrows(InvalidDateFormatException.class,
          () -> logController.getLogsByDate(futureDateString));
      assertTrue(exception.getMessage().contains("Дата не может быть в будущем"));
    }
  }

  @Test
  void testGetLogsByDate_LogFileNotFound() {
    try (MockedStatic<Paths> pathsMock = mockStatic(Paths.class);
         MockedStatic<Files> filesMock = mockStatic(Files.class)) {
      String dateString = "2025-03-30";

      pathsMock.when(() -> Paths.get("./test-logs", "web-library.log")).thenReturn(logFile);
      filesMock.when(() -> Files.exists(logFile)).thenReturn(false);

      LogsNotFoundException exception = assertThrows(LogsNotFoundException.class,
          () -> logController.getLogsByDate(dateString));
      assertEquals("Файл логов не найден.", exception.getMessage());
    }
  }

  @Test
  void testGetLogsByDate_NoLogsForDate() throws IOException {
    try (MockedStatic<Paths> pathsMock = mockStatic(Paths.class);
         MockedStatic<Files> filesMock = mockStatic(Files.class)) {
      String dateString = "2025-03-30";
      List<String> logLines = List.of("2025-03-29 10:00:00 INFO Test log entry");

      pathsMock.when(() -> Paths.get("./test-logs", "web-library.log")).thenReturn(logFile);
      filesMock.when(() -> Files.exists(logFile)).thenReturn(true);
      filesMock.when(() -> Files.lines(logFile)).thenReturn(logLines.stream());

      LogsNotFoundException exception = assertThrows(LogsNotFoundException.class,
          () -> logController.getLogsByDate(dateString));
      assertEquals("Логов за 2025-03-30 не найдено.", exception.getMessage());
    }
  }

  @Test
  void testGetLogsByDate_ProcessingError() throws IOException {
    try (MockedStatic<Paths> pathsMock = mockStatic(Paths.class);
         MockedStatic<Files> filesMock = mockStatic(Files.class)) {
      String dateString = "2025-03-31";

      pathsMock.when(() -> Paths.get("./test-logs", "web-library.log")).thenReturn(logFile);
      filesMock.when(() -> Files.exists(logFile)).thenReturn(true);
      filesMock.when(() -> Files.lines(logFile)).thenThrow(new IOException("IO Error"));

      LogProcessingException exception = assertThrows(LogProcessingException.class,
          () -> logController.getLogsByDate(dateString));
      assertEquals("Ошибка при обработке логов.", exception.getMessage());
    }
  }
}