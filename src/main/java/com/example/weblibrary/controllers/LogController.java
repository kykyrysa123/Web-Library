package com.example.weblibrary.controllers;

import com.example.weblibrary.exception.InvalidDateFormatException;
import com.example.weblibrary.exception.LogProcessingException;
import com.example.weblibrary.exception.LogsNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling log file operations.
 * Provides endpoints for retrieving log files filtered by date.
 */
@RestController
@RequestMapping("/api/logs")
public class LogController {

  @Value("${logging.file.path:./logs}")
  private String logPath;

  /**
   * Retrieves log entries for a specific date.
   *
   * @param dateString the date in YYYY-MM-DD format
   * @return ResponseEntity containing the log file as attachment
   * @throws InvalidDateFormatException if date format is invalid
   * @throws LogsNotFoundException if no logs found for specified date
   * @throws LogProcessingException if error occurs while processing logs
   */
  @GetMapping
  public ResponseEntity<byte[]> getLogsByDate(
      @RequestParam(name = "date") String dateString) {

    LocalDate date = parseDate(dateString);

    if (date.isAfter(LocalDate.now())) {
      throw new InvalidDateFormatException(
          "Дата не может быть в будущем: " + date);
    }

    String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    Path logFile = Paths.get(logPath, "web-library.log");
    Path datedLogFile = Paths.get(logPath, "log-" + formattedDate + ".log");

    if (!Files.exists(logFile)) {
      throw new LogsNotFoundException("Файл логов не найден.");
    }

    try (Stream<String> lines = Files.lines(logFile)) {
      List<String> filteredLogs = lines
          .filter(line -> line.startsWith(formattedDate))
          .toList();

      if (filteredLogs.isEmpty()) {
        throw new LogsNotFoundException(
            "Логов за " + formattedDate + " не найдено.");
      }

      Files.write(datedLogFile, filteredLogs,
          StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING);

      byte[] fileContent = Files.readAllBytes(datedLogFile);
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=" + datedLogFile.getFileName());

      return ResponseEntity.ok()
                           .headers(headers)
                           .body(fileContent);

    } catch (IOException e) {
      throw new LogProcessingException("Ошибка при обработке логов.");
    }
  }

  /**
   * Parses date string into LocalDate object.
   *
   * @param dateString the date string to parse
   * @return parsed LocalDate object
   * @throws InvalidDateFormatException if date format is invalid
   */
  private LocalDate parseDate(String dateString) {
    try {
      return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (Exception e) {
      throw new InvalidDateFormatException(
          "Дата указана в неверном формате. Ожидаемый формат: YYYY-MM-DD");
    }
  }
}