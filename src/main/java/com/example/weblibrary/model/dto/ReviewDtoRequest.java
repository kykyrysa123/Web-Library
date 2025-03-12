package com.example.weblibrary.model.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for creating or updating a review.
 *
 * @param id          The review ID.
 * @param bookId      The ID of the book being reviewed.
 * @param userId      The ID of the user submitting the review.
 * @param rating      The rating given to the book.
 * @param reviewText  The text content of the review.
 * @param reviewDate  The date the review was written.
 */
public record ReviewDtoRequest(
    Long id,
    Long bookId,
    Long userId,
    Double rating,
    String reviewText,
    LocalDate reviewDate) {
}
