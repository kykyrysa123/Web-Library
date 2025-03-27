package com.example.weblibrary.exception;

import java.util.Map;

public class CustomValidationException extends RuntimeException {
  private final Map<String, String> errorDetails;

  public CustomValidationException(String message,
      Map<String, String> errorDetails
  ) {
    super(message);
    this.errorDetails = errorDetails;
  }

  public Map<String, String> getErrorDetails() {
    return errorDetails;
  }
}