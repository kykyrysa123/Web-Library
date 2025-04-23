package com.example.weblibrary.model.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) for representing book details in responses.
 * This record encapsulates book information returned to the client.
 */
public record BookDtoResponse(
    Long id,
    String title,
    String publisher,
    String isbn,
    Integer pages,
    String genre,
    LocalDate publishDate,
    String language,
    String description,
    String imageUrl,
    String readUrl,
    Double rating,
    List<Long> authorIds,
    List<AuthorDtoResponse> authors
) {
}