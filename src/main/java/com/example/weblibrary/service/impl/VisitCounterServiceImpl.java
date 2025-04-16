package com.example.weblibrary.service.impl;

import com.example.weblibrary.model.dto.VisitLog;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/**
 * Service implementation for tracking and counting website visits.
 * Provides thread-safe visit counting and logging functionality.
 */
@Service
public class VisitCounterServiceImpl {

  private final AtomicLong counter = new AtomicLong(0);
  private final List<VisitLog> visitLogs = new ArrayList<>();

  /**
   * Increments the visit counter and logs the visit details.
   *
   * @param url the visited URL
   * @param request the HTTP request containing client information
   * @return the new total visit count
   */
  public synchronized long incrementCounter(String url, HttpServletRequest request) {
    long visitNumber = counter.incrementAndGet();
    String clientIp = request.getRemoteAddr();

    VisitLog visitLog = new VisitLog(
        LocalDateTime.now(),
        url,
        clientIp,
        visitNumber
    );

    visitLogs.add(visitLog);
    return visitNumber;
  }

  /**
   * Gets the current total visit count.
   *
   * @return the total number of visits
   */
  public synchronized long getCounter() {
    return counter.get();
  }

  /**
   * Gets a copy of all visit logs.
   *
   * @return a list of all visit logs
   */
  public synchronized List<VisitLog> getVisitLogs() {
    return new ArrayList<>(visitLogs);
  }
}