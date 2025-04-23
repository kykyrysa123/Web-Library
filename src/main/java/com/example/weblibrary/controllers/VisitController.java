package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.VisitLog;
import com.example.weblibrary.service.impl.VisitCounterServiceImpl;
import com.google.common.util.concurrent.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling visit tracking operations.
 * Provides endpoints for tracking visits, getting visit counts and logs.
 */
@RestController
@RequestMapping("/api/visits")
@Tag(name = "Visits API", description = "API для отслеживания посещений и статистики")
public class VisitController {

  /**
   * Service for handling visit counting operations.
   */
  private final VisitCounterServiceImpl visitCounterService;

  private final RateLimiter rateLimiter = RateLimiter.create(10.0); // 10 requests per second

  /**
   * Rate limiter to control the number of requests per second.
   * Allows 10 requests per second.
   */
  @Autowired
  public VisitController(VisitCounterServiceImpl visitCounterService) {
    this.visitCounterService = visitCounterService;
  }

  /**
   * Tracks a visit to the specified URL.
   *
   * @param url the URL being visited
   * @param request the HTTP request object
   * @return ResponseEntity with visit information
   */
  @GetMapping("/track")
  @Operation(
      summary = "Зарегистрировать посещение URL",
      description = "Увеличивает счетчик посещений для указанного URL "
          + "и возвращает информацию о посещении",
      responses = {
          @ApiResponse(responseCode = "200", description = "Посещение успешно зарегистрировано"),
          @ApiResponse(responseCode = "429", description = "Слишком много запросов")
      }
  )
  public ResponseEntity<Map<String, Object>> trackVisit(
      @Parameter(description = "URL для отслеживания", required = true)
      @RequestParam String url,
      HttpServletRequest request) {

    if (!rateLimiter.tryAcquire()) {
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("message", "Too many requests. Please try again later.");
      return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
    }

    long visitCount = visitCounterService.incrementCounter(url, request);

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Visit recorded successfully!");
    response.put("totalVisits", visitCount);
    response.put("url", url);
    response.put("ip", request.getRemoteAddr());

    return ResponseEntity.ok(response);
  }

  /**
   * Gets the total number of visits.
   *
   * @return ResponseEntity with total visit count
   */
  @GetMapping("/count")
  @Operation(
      summary = "Получить общее количество посещений",
      description = "Возвращает общее количество зарегистрированных посещений всех URL",
      responses = {
          @ApiResponse(responseCode = "200", description = "Успешный запрос количества посещений")
      }
  )
  public ResponseEntity<Map<String, Object>> getVisitCount() {
    long count = visitCounterService.getCounter();

    Map<String, Object> response = new HashMap<>();
    response.put("totalVisitors", count);
    response.put("message", "Current visitor count");

    return ResponseEntity.ok(response);
  }

  /**
   * Gets the list of all visit logs.
   *
   * @return ResponseEntity with list of visit logs
   */
  @GetMapping("/logs")
  @Operation(
      summary = "Получить журнал посещений",
      description = "Возвращает список всех зарегистрированных посещений с деталями",
      responses = {
          @ApiResponse(responseCode = "200", description = "Успешный запрос журнала посещений")
      }
  )
  public ResponseEntity<List<VisitLog>> getVisitLogs() {
    List<VisitLog> logs = visitCounterService.getVisitLogs();
    return ResponseEntity.ok(logs);
  }
}