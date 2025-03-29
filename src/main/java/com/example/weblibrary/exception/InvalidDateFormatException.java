package com.example.weblibrary.exception;

/**
 * Exception thrown when date format is invalid.
 * This exception is used to indicate that a provided date string
 * does not match the expected format.
 */
public class InvalidDateFormatException extends RuntimeException {

  /**
   * Constructs a new InvalidDateFormatException with the specified detail message.
   *
   * @param message the detail message explaining the date format error
   */
  public InvalidDateFormatException(String message) {
    super(message);
  }
}