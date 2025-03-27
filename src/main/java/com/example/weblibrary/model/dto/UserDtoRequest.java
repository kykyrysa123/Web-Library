package com.example.weblibrary.model.dto;

import jakarta.validation.constraints.*;

/**
 * Data transfer object for creating a user.
 */
public record UserDtoRequest(
    @Null(message = "ID should not be provided for new users")
    Long id,

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    String name,

    @NotBlank(message = "Surname is required")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    String surname,

    @Size(max = 50, message = "Patronymic must not exceed 50 characters")
    String patronymic,

    @Min(value = 12, message = "Age must be at least 12")
    @Max(value = 120, message = "Age must not exceed 120")
    Integer age,

    @Size(max = 50, message = "Subscription must not exceed 50 characters")
    String subscription,

    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Sex must be MALE, FEMALE or OTHER")
    String sex,

    @Size(max = 50, message = "Country must not exceed 50 characters")
    String country,

    @Size(max = 500, message = "Favorite books list must not exceed 500 characters")
    String favoriteBooks,

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
        message = "Password must contain at least one digit, one lowercase, one uppercase letter and one special character"
    )
    String passwordHash
) {}