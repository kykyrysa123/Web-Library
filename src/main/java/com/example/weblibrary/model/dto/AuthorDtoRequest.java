package com.example.weblibrary.model.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Request DTO for creating/updating author with validation constraints
 */
public record AuthorDtoRequest(
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name,

    @NotBlank(message = "Surname cannot be blank")
    @Size(max = 100, message = "Surname must not exceed 100 characters")
    String surname,

    @Size(max = 100, message = "Patronymic must not exceed 100 characters")
    String patronymic,

    @Past(message = "Birth date must be in the past")
    LocalDate birthDate,

    @PastOrPresent(message = "Death date must be in past or present")
    LocalDate deathDate,

    @Size(max = 2000, message = "Biography must not exceed 2000 characters")
    String biography,

    @Size(max = 100, message = "Genre specialization must not exceed 100 characters")
    String genreSpecialization,

    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
    Double rating
) {
    public AuthorDtoRequest(String name) {
        this( name, null, null, null, null, null, null, null);
    }
}