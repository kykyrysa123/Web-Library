package com.example.weblibrary.service.impl;

import com.example.weblibrary.exception.LogFileNotReadyException;
import com.example.weblibrary.exception.LogProcessingException;
import com.example.weblibrary.model.Log;
import com.example.weblibrary.model.dto.LogDto;
import com.example.weblibrary.repository.LogRepository;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service for managing application logs. Provides functionality for creating
 * log files asynchronously, checking log status and retrieving log files.
 */
@Service
public class LogService {

  private static final String LOG_DIRECTORY = "logs";
  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss.SSS");

  @Autowired
  private LogRepository logRepository;

  /**
   * Asynchronously creates a new log file.
   *
   * @return CompletableFuture containing the ID of the created log
   */
  @Async
  public CompletableFuture<Long> createLogFileAsync() {
    return CompletableFuture.supplyAsync(() -> {
      Log log = new Log();
      log.setStatus("PENDING");
      log = logRepository.save(log);

      try {
        Thread.sleep(10000);

        String fileName = String.format("application-log-%d-%s.txt",
            log.getId(), LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")));

        File logFile = createLogFile(fileName);
        writeLogContent(logFile, log.getId());

        log.setStatus("COMPLETED");
        log.setFilePath(logFile.getAbsolutePath());
        logRepository.save(log);

        return log.getId();
      } catch (Exception e) {
        log.setStatus("FAILED");
        logRepository.save(log);
        throw new LogProcessingException("Failed to create log file", e);
      }
    });
  }

  private File createLogFile(String fileName) throws IOException {
    File logsDir = new File(LOG_DIRECTORY);
    if (!logsDir.exists() && !logsDir.mkdirs()) {
      throw new IOException("Failed to create logs directory");
    }

    File logFile = new File(logsDir, fileName);
    if (!logFile.createNewFile()) {
      throw new IOException("Log file already exists");
    }
    return logFile;
  }

  private void writeLogContent(File logFile, Long logId) throws IOException {
    try (FileWriter writer = new FileWriter(logFile)) {
      String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);

      writer.write(String.format("=== APPLICATION LOGS [ID: %d] ===\n", logId));
      writer.write(String.format("=== Generated at: %s ===\n\n", timestamp));

      writer.write(
          String.format("[INFO] %s - Application started\n", timestamp));
      writer.write(
          String.format("[DEBUG] %s - Initializing components for log ID: %d\n",
              timestamp, logId));
      writer.write(
          String.format("[INFO] %s - Database connection established\n",
              timestamp));
      writer.write(
          String.format("[WARN] %s - Cache size exceeds threshold (85%%)\n",
              timestamp));
      writer.write(String.format(
          "[ERROR] %s - Failed to connect to external service - retrying...\n",
          timestamp));
      writer.write(String.format(
          "[INFO] %s - Log file generation completed successfully\n",
          timestamp));
    }
  }

  /**
   * Retrieves the status of a log generation task.
   *
   * @param id
   *     the ID of the log task
   * @return LogDto containing log status information
   * @throws LogProcessingException
   *     if log with specified ID is not found
   */
  public LogDto getLogStatus(Long id) {
    Log log = logRepository.findById(id).orElseThrow(
        () -> new LogProcessingException("Log not found with ID: " + id));

    LogDto dto = new LogDto();
    dto.setId(log.getId());
    dto.setStatus(log.getStatus());
    dto.setFilePath(log.getFilePath());
    return dto;
  }

  /**
   * Retrieves the generated log file.
   *
   * @param id
   *     the ID of the log task
   * @return File object representing the log file
   * @throws LogProcessingException
   *     if log file is not found
   * @throws LogFileNotReadyException
   *     if log file is not ready for download
   */
  public File getLogFile(Long id) {
    Log log = logRepository.findById(id).orElseThrow(
        () -> new LogProcessingException("Log not found with ID: " + id));

    if (!"COMPLETED".equals(log.getStatus())) {
      throw new LogFileNotReadyException(
          "Log file is not ready. Current status: " + log.getStatus());
    }

    File logFile = new File(log.getFilePath());
    if (!logFile.exists()) {
      throw new LogProcessingException("Log file not found on disk");
    }

    return logFile;
  }
}