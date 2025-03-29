package com.example.weblibrary.model.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing log file response.
 * Contains log date and content for API responses.
 */
@Data
public class LogResponse {
  /**
   * The date of the log entries in YYYY-MM-DD format.
   */
  private String date;

  /**
   * The content of the log entries for the specified date.
   */
  private String logContent;
}