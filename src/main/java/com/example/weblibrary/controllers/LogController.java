package com.example.weblibrary.controllers;

import com.example.weblibrary.exception.InvalidDateFormatException;
import com.example.weblibrary.exception.LogFileNotReadyException;
import com.example.weblibrary.exception.LogProcessingException;
import com.example.weblibrary.model.dto.LogDto;
import com.example.weblibrary.service.impl.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Контроллер для работы с журналами приложения.
 * Предоставляет API для создания, проверки статуса и загрузки файлов журналов.
 */
@RestController
@RequestMapping("/api/logs")
@Tag(name = "Logs", description = "API для управления журналами приложений")
public class LogController {

  @Autowired
  private LogService logService;

  /**
   * Асинхронно создает новый файл журнала.
   *
   * @return CompletableFuture с ResponseEntity, содержащим ID задачи создания журнала
   */
  @PostMapping("/create")
  @Operation(summary = "Асинхронное создание нового файла журнала")
  public CompletableFuture<ResponseEntity<Long>> createLogFile() {
    return logService.createLogFileAsync()
                     .thenApply(ResponseEntity::ok)
                     .exceptionally(ex -> {
                       Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                       throw new ResponseStatusException(
                           HttpStatus.INTERNAL_SERVER_ERROR,
                           "Failed to create log file: " + cause.getMessage());
                     });
  }

  /**
   * Получает статус генерации журнала по ID задачи.
   *
   * @param id ID задачи генерации журнала
   * @return ResponseEntity с информацией о статусе
   */
  @GetMapping("/{id}/status")
  @Operation(summary = "Получение статуса генерации журнала по идентификатору")
  public ResponseEntity<LogDto> getLogStatus(
      @Parameter(name = "id", description = "Log task ID", required = true, in = ParameterIn.PATH)
      @PathVariable Long id) {
    try {
      LogDto logDto = logService.getLogStatus(id);
      return ResponseEntity.ok(logDto);
    } catch (LogProcessingException ex) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
    }
  }

  /**
   * Загружает файл журнала по ID задачи.
   *
   * @param id ID задачи генерации журнала
   * @return ResponseEntity с файлом журнала
   */
  @GetMapping("/{id}/file")
  @Operation(summary = "Загрузка файла журнала по идентификатору")
  public ResponseEntity<Resource> getLogFile(
      @Parameter(name = "id", description = "Log task ID", required = true, in = ParameterIn.PATH)
      @PathVariable Long id) {
    try {
      File logFile = logService.getLogFile(id);
      Resource resource = new FileSystemResource(logFile);

      return ResponseEntity.ok()
                           .contentType(MediaType.TEXT_PLAIN)
                           .header(
                               HttpHeaders.CONTENT_DISPOSITION,
                               "attachment; filename=\"" + logFile.getName() + "\"")
                           .body(resource);
    } catch (LogFileNotReadyException ex) {
      throw new ResponseStatusException(HttpStatus.ACCEPTED, ex.getMessage());
    } catch (LogProcessingException ex) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
    }
  }

  /**
   * Получает журналы за указанную дату.
   *
   * @param date Дата в формате YYYY-MM-DD
   * @return ResponseEntity с содержимым журнала
   */
  @GetMapping
  @Operation(summary = "Получение журналов по дате")
  public ResponseEntity<byte[]> getLogsByDate(
      @Parameter(name = "date", description = "Date in YYYY-MM-DD format",
          required = true, in = ParameterIn.QUERY)
      @RequestParam String date) {
    try {
      LocalDate logDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
      if (logDate.isAfter(LocalDate.now())) {
        throw new InvalidDateFormatException("Date cannot be in the future");
      }

      String sampleLogs = String.format("=== Logs for %s ===\n\n"
          + "[INFO] Application started\n"
          + "[DEBUG] Initializing components\n"
          + "[INFO] Database connection established\n", logDate);

      return ResponseEntity.ok()
                           .header(HttpHeaders.CONTENT_DISPOSITION,
                               "attachment; filename=logs-" + date + ".txt")
                           .contentType(MediaType.TEXT_PLAIN)
                           .body(sampleLogs.getBytes());
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invalid date format. Use YYYY-MM-DD");
    }
  }
}