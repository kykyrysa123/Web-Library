package com.example.weblibrary.model.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Request DTO for creating/updating author with validation constraints.
 */
public record AuthorDtoRequest(
    @Size(max = 100, message =
        "Name must not exceed 100 characters") String name,

    @Size(max = 100, message =
        "Surname must not exceed 100 characters") String surname,

    @Size(max = 100, message = "Patronymic must not exceed 100 characters") String patronymic,

    @Past(message = "Birth date must be in the past") LocalDate birthDate,

    @PastOrPresent(message = "Death date must be in past or present") LocalDate deathDate,

    @Size(max = 2000, message = "Biography must not exceed 2000 characters") String biography,

    @Size(max = 100, message = "Genre specialization must not exceed 100 "
        + "characters") String genreSpecialization,

    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5.0") Double rating) {
  /**
   * Constructor with name only for simplified author creation.
   *
   * @param name
   *     the author's name
   */
  public AuthorDtoRequest(String name) {
    this(name, null, null, null, null, null, null, null);
  }
}