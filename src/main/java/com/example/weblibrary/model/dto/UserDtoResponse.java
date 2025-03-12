package com.example.weblibrary.model.dto;

import java.time.LocalDate;

/**
 * Data transfer object for responding with user information.
 * This record is used to send user data in responses.
 */
public record UserDtoResponse(
    Integer id,
    String name,
    String surname,
    String patronymic,
    Integer age,
    String subscription,
    String sex,
    String country,
    String favoriteBooks,
    String email,
    String passwordHash,
    LocalDate registrationDate,
    LocalDate lastLogin
) {}
