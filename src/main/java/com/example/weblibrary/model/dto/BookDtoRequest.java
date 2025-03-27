package com.example.weblibrary.model.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

/**
 * Represents a request data transfer object (DTO) for creating or updating a book.
 */
public record BookDtoRequest(
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    String title,

    @NotBlank(message = "Publisher cannot be blank")
    @Size(max = 100, message = "Publisher must not exceed 100 characters")
    String publisher,

    String isbn,

    @PositiveOrZero(message = "Page count cannot be negative")
    @Max(value = 10000, message = "Page count cannot exceed 10,000")
    Integer pages,

    @NotBlank(message = "Genre cannot be blank")
    @Size(max = 50, message = "Genre must not exceed 50 characters")
    String genre,

    @PastOrPresent(message = "Publish date cannot be in the future")
    LocalDate publishDate,

    @NotBlank(message = "Language cannot be blank")
    @Size(min = 2, max = 30, message = "Language must be between 2 and 30 characters")
    String language,

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    String description,

    @URL(message = "Image URL must be valid")
    String imageUrl,

    @DecimalMin(value = "0.0", message = "Rating cannot be less than 0")
    @DecimalMax(value = "5.0", message = "Rating cannot be greater than 5")
    Double rating,

    @NotNull(message = "Author ID cannot be null")
    @Positive(message = "Author ID must be positive")
    Long authorId
) {}