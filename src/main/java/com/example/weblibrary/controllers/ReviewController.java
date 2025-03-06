package com.example.weblibrary.controllers;

import com.example.weblibrary.model.Review;
import com.example.weblibrary.service.impl.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
  private final ReviewServiceImpl reviewService;

  @GetMapping
  public ResponseEntity<List<Review>> getAllReviews() {
    return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Review> getReviewById(@PathVariable int id) {
    return reviewService.getById(id).map(
        review -> new ResponseEntity<>(review, HttpStatus.OK)).orElseGet(
        () -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  public ResponseEntity<Review> createReview(@RequestBody Review review) {
    return new ResponseEntity<>(reviewService.create(review),
        HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Review> updateReview(@PathVariable int id,
      @RequestBody Review review
  ) {
    return new ResponseEntity<>(reviewService.update(id, review),
        HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReview(@PathVariable int id) {
    reviewService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
