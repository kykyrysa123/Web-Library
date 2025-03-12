package com.example.weblibrary.model.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for returning review details.
 *
 * @param id          The review ID.
 * @param book        The reviewed book details.
 * @param user        The user who wrote the review.
 * @param rating      The rating given to the book.
 * @param reviewText  The text content of the review.
 * @param reviewDate  The date the review was written.
 */
public record ReviewDtoResponse(
    Long id,
    BookDtoResponse book,
    UserDtoResponse user,
    Double rating,
    String reviewText,
    LocalDate reviewDate) {
}
