package com.example.weblibrary.model.dto;


/**
 * Data transfer object for creating a user.
 * This record is used to capture user information during user creation.
 */
public record UserDtoRequest(
    Long id,
    String name,
    String surname,
    String patronymic,
    Integer age,
    String subscription,
    String sex,
    String country,
    String favoriteBooks,
    String email,
    String passwordHash
) {
}
