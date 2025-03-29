package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.ReviewDtoRequest;
import com.example.weblibrary.model.dto.ReviewDtoResponse;
import com.example.weblibrary.service.impl.ReviewServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing reviews.
 * Provides endpoints for creating, reading, updating and deleting book reviews.
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Управление отзывами")
public class ReviewController {

  private final ReviewServiceImpl reviewService;

  /**
   * Retrieves all reviews.
   *
   * @return ResponseEntity containing list of all reviews
   */
  @GetMapping
  @Operation(summary = "Получить все отзывы",
      description = "Возвращает список всех отзывов")
  public ResponseEntity<List<ReviewDtoResponse>> getAllReviews() {
    return ResponseEntity.ok(reviewService.getAll());
  }

  /**
   * Retrieves a review by its ID.
   *
   * @param id the ID of the review to retrieve
   * @return ResponseEntity containing the review data
   */
  @GetMapping("/{id}")
  @Operation(summary = "Получить отзыв по ID",
      description = "Возвращает отзыв по его идентификатору")
  public ResponseEntity<ReviewDtoResponse> getReviewById(@PathVariable Long id) {
    return ResponseEntity.ok(reviewService.getById(id));
  }

  /**
   * Creates a new review.
   *
   * @param request DTO containing review data
   * @return ResponseEntity containing the created review
   */
  @PostMapping
  @Operation(summary = "Создать отзыв",
      description = "Создаёт новый отзыв и возвращает его данные")
  public ResponseEntity<ReviewDtoResponse> createReview(
      @Valid @RequestBody ReviewDtoRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(reviewService.create(request));
  }

  /**
   * Updates an existing review.
   *
   * @param id the ID of the review to update
   * @param request DTO containing updated review data
   * @return ResponseEntity containing the updated review
   */
  @PutMapping("/{id}")
  @Operation(summary = "Обновить отзыв",
      description = "Обновляет информацию об отзыве по его идентификатору")
  public ResponseEntity<ReviewDtoResponse> updateReview(
      @PathVariable Long id,
      @Valid @RequestBody ReviewDtoRequest request) {
    return ResponseEntity.ok(reviewService.update(id, request));
  }

  /**
   * Deletes a review.
   *
   * @param id the ID of the review to delete
   * @return ResponseEntity with no content
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить отзыв",
      description = "Удаляет отзыв по его идентификатору")
  public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
    reviewService.delete(id);
    return ResponseEntity.noContent().build();
  }
}