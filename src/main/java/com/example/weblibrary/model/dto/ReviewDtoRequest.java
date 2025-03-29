package com.example.weblibrary.model.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for creating or updating a review. Contains
 * validation constraints for review data.
 */
public record ReviewDtoRequest(
    @Null(message = "ID should not be provided for new reviews") Long id,

    @NotNull(message = "Book ID is required") @Positive(message = "Book ID "
        + "must be positive") Long bookId,

    @NotNull(message = "User ID is required") @Positive(message = "User ID "
        + "must be positive") Long userId,

    @NotNull(message = "Rating is required") @DecimalMin(value = "0.5",
        message = "Rating must be at least 0.5") @DecimalMax(value = "5.0",
        message = "Rating must be at most 5.0") Double rating,

    @Size(max = 2000,
        message = "Review text must not exceed 2000 characters") String reviewText,

    @PastOrPresent(message = "Review date cannot be in the future") LocalDate reviewDate) {
  /**
   * Constructor for creating a review with just review text.
   *
   * @param reviewText
   *     the text content of the review
   */
  public ReviewDtoRequest(String reviewText) {
    this(null, null, null, null, reviewText, null);
  }
}