package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.ReviewDtoRequest;
import com.example.weblibrary.model.dto.ReviewDtoResponse;
import com.example.weblibrary.service.impl.ReviewServiceImpl;
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
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewServiceImpl reviewService;

  /**
   * Retrieves all reviews.
   *
   * @return A list of all reviews.
   */
  @GetMapping
  public ResponseEntity<List<ReviewDtoResponse>> getAllReviews() {
    return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
  }

  /**
   * Retrieves a review by its ID.
   *
   * @param id The ID of the review.
   * @return The found review.
   */
  @GetMapping("/{id}")
  public ResponseEntity<ReviewDtoResponse> getReviewById(@PathVariable Long id) {
    return new ResponseEntity<>(reviewService.getById(id), HttpStatus.OK);
  }

  /**
   * Creates a new review.
   *
   * @param reviewDtoRequest The review data.
   * @return The created review.
   */
  @PostMapping
  public ResponseEntity<ReviewDtoResponse> createReview(
      @RequestBody ReviewDtoRequest reviewDtoRequest) {
    return new ResponseEntity<>(reviewService.create(reviewDtoRequest), HttpStatus.CREATED);
  }

  /**
   * Updates an existing review.
   *
   * @param id               The ID of the review to update.
   * @param reviewDtoRequest The updated review data.
   * @return The updated review.
   */
  @PutMapping("/{id}")
  public ResponseEntity<ReviewDtoResponse> updateReview(
      @PathVariable Long id, @RequestBody ReviewDtoRequest reviewDtoRequest) {
    return new ResponseEntity<>(reviewService.update(id, reviewDtoRequest), HttpStatus.OK);
  }

  /**
   * Deletes a review by its ID.
   *
   * @param id The ID of the review to delete.
   * @return A response with no content.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
    reviewService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
