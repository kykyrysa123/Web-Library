package com.example.weblibrary.model.dto;

import java.time.LocalDate;

/**
 * Represents a request data transfer object (DTO) for creating or updating an author.
 * This record encapsulates the necessary details about an author, such as their name,
 * birth date, biography, and other relevant information.
 *
 * @param name               the first name of the author.
 * @param surname            the last name of the author.
 * @param patronymic         the patronymic (middle name) of the author.
 * @param birthDate          the birth date of the author.
 * @param deathDate          the death date of the author (if applicable).
 * @param biography          a brief biography of the author.
 * @param genreSpecialization the genre(s) the author specializes in.
 * @param rating             the rating of the author.
 */
public record AuthorDtoRequest(
    String name,
    String surname,
    String patronymic,
    LocalDate birthDate,
    LocalDate deathDate,
    String biography,
    String genreSpecialization,
    Double rating
) {}