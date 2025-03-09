package com.example.weblibrary.controllers;

import com.example.weblibrary.model.Review;
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
 * Controller to handle the CRUD operations for reviews in the web library system.
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewServiceImpl reviewService;

  /**
   * Get all reviews.
   *
   * @return A list of all reviews.
   */
  @GetMapping
  public ResponseEntity<List<Review>> getAllReviews() {
    return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
  }

  /**
   * Get a review by its ID.
   *
   * @param id The ID of the review.
   * @return The review if found, otherwise a NOT_FOUND response.
   */
  @GetMapping("/{id}")
  public ResponseEntity<Review> getReviewById(@PathVariable int id) {
    return reviewService.getById(id)
                        .map(review -> new ResponseEntity<>(review, HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * Create a new review.
   *
   * @param review The review to be created.
   * @return The created review.
   */
  @PostMapping
  public ResponseEntity<Review> createReview(@RequestBody Review review) {
    return new ResponseEntity<>(reviewService.create(review), HttpStatus.CREATED);
  }

  /**
   * Update an existing review.
   *
   * @param id     The ID of the review to be updated.
   * @param review The review data to update.
   * @return The updated review.
   */
  @PutMapping("/{id}")
  public ResponseEntity<Review> updateReview(@PathVariable int id, @RequestBody Review review) {
    return new ResponseEntity<>(reviewService.update(id, review), HttpStatus.OK);
  }

  /**
   * Delete a review by its ID.
   *
   * @param id The ID of the review to be deleted.
   * @return A response indicating the deletion was successful.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReview(@PathVariable int id) {
    reviewService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
