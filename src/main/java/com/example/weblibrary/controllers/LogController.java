package com.example.weblibrary.controllers;

import com.example.weblibrary.exception.InvalidDateFormatException;
import com.example.weblibrary.exception.LogsNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/logs")
public class LogController {

  @Value("${logging.file.path:./logs}")
  private String logPath;

  @GetMapping
  public ResponseEntity<?> getLogsByDate(@RequestParam(name = "date") String dateString) {

    LocalDate date = parseDate(dateString);


    if (date.isAfter(LocalDate.now())) {
      throw new InvalidDateFormatException("Дата не может быть в будущем: " + date);
    }


    String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    Path logFile = Paths.get(logPath, "web-library.log");
    Path datedLogFile = Paths.get(logPath, "log-" + formattedDate + ".log");


    if (!Files.exists(logFile)) {
      throw new LogsNotFoundException("Файл логов не найден.");
    }


    try (Stream<String> lines = Files.lines(logFile)) {
      List<String> filteredLogs = lines
          .filter(line -> line.startsWith(formattedDate)) // Фильтруем строки по дате
          .collect(Collectors.toList());


      if (filteredLogs.isEmpty()) {
        throw new LogsNotFoundException("Логов за " + formattedDate + " не найдено.");
      }


      Files.write(datedLogFile, filteredLogs, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);


      byte[] fileContent = Files.readAllBytes(datedLogFile);
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + datedLogFile.getFileName());

      return ResponseEntity.ok().headers(headers).body(fileContent);

    } catch (IOException e) {
      throw new RuntimeException("Ошибка при обработке логов.");
    }
  }

  private LocalDate parseDate(String dateString) {
    try {
      return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (Exception e) {
      throw new InvalidDateFormatException("Дата указана в неверном формате. Ожидаемый формат: YYYY-MM-DD");
    }
  }
}
