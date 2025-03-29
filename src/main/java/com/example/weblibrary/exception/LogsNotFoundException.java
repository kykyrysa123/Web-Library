package com.example.weblibrary.exception;

/**
 * Exception thrown when requested log files cannot be found.
 * This exception indicates that the specified log files either don't exist
 * or contain no entries for the requested criteria.
 */
public class LogsNotFoundException extends RuntimeException {

  /**
   * Constructs a new LogsNotFoundException with the specified detail message.
   *
   * @param message the detail message explaining which logs weren't found
   */
  public LogsNotFoundException(String message) {
    super(message);
  }
}