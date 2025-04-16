package com.example.weblibrary.model.dto;

/**
 * Data Transfer Object for Log information.
 * Contains log id, status and file path.
 */
public class LogDto {
  private Long id;
  private String status; // "PENDING", "COMPLETED", "FAILED"
  private String filePath; // Путь к файлу, если он создан

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }
}