package com.example.weblibrary.model.dto;

import java.time.LocalDateTime;
/**
 visitLog.
 */

public class VisitLog {
  private LocalDateTime timestamp;
  private String url;
  private String ip;
  private long visitNumber;

  /**
   visitLog.
   * with
   * timestamp;
   * url;
   * ip;
   * visitNumber
   */

  public VisitLog(LocalDateTime timestamp, String url, String ip, long visitNumber) {
    this.timestamp = timestamp;
    this.url = url;
    this.ip = ip;
    this.visitNumber = visitNumber;
  }

  // Геттеры и сеттеры
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public long getVisitNumber() {
    return visitNumber;
  }

  public void setVisitNumber(long visitNumber) {
    this.visitNumber = visitNumber;
  }
}