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

  /**
   * Constructs a new LogProcessingException with the specified detail message and cause.
   *
   * @param message the detail message explaining the log processing error
   * @param cause the underlying cause of this exception
   */
  public LogProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}