package com.example.weblibrary.exception;

/**
 * Exception thrown when an error occurs during log file processing.
 * This exception indicates problems with reading, filtering, or writing log files.
 */
public class LogProcessingException extends RuntimeException {

  /**
   * Constructs a new LogProcessingException with the specified detail message.
   *
   * @param message the detail message explaining the log processing error
   */
  public LogProcessingException(String message) {
    super(message);
  }
}