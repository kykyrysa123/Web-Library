package com.example.weblibrary.model.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a response data transfer object (DTO) for an author.
 * This record encapsulates the details about an author, including their ID, name,
 * birthdate, biography, and other relevant information.
 *
 * @param id                 the unique identifier of the author.
 * @param name               the first name of the author.
 * @param surname            the last name of the author.
 * @param patronymic         the patronymic (middle name) of the author.
 * @param birthDate          the birthdate of the author.
 * @param deathDate          the death date of the author (if applicable).
 * @param biography          a brief biography of the author.
 * @param genreSpecialization the genre(s) the author specializes in.
 * @param rating             the rating of the author.
 */
public record AuthorDtoResponse(
    Long id,
    String name,
    String surname,
    String patronymic,
    LocalDate birthDate,
    LocalDate deathDate,
    String biography,
    String genreSpecialization,
    Double rating,
    List<BookDtoResponse> books
) {
  public AuthorDtoResponse(Long id, String name) {
    this(id, name, null, null, null, null, null, null, null, null);
  }
}