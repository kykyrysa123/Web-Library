package com.example.weblibrary.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data transfer object for creating a user. Contains validation constraints for
 * user registration and updates.
 */
public record UserDtoRequest(
    @Null(message = "ID should not be provided for new users") Long id,

    @NotBlank(message = "Name is required") @Size(min = 2, max = 50, message
        = "Name must be between 2 and 50 characters") String name,

    @NotBlank(message = "Surname is required") @Size(min = 2, max = 50,
        message = "Surname must be between 2 and 50 characters") String surname,

    @Size(max = 50, message = "Patronymic must not exceed 50 characters") String patronymic,

    @Min(value = 12, message = "Age must be at least 12") @Max(value = 120,
        message = "Age must not exceed 120") Integer age,

    @Size(max = 50, message = "Subscription must not exceed 50 characters") String subscription,

    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Sex must be MALE, "
        + "FEMALE or OTHER") String sex,

    @Size(max = 50, message = "Country must not exceed 50 characters") String country,

    @Size(max = 500, message = "Favorite books list must not exceed 500 "
        + "characters") String favoriteBooks,

    @NotBlank(message = "Email is required") @Email(message = "Email should "
        + "be valid") @Size(max = 100, message = "Email must not exceed 100 "
        + "characters") String email,

    @NotBlank(message = "Password is required") @Size(min = 8, max = 100,
        message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message =
        "Password must contain at least one digit, one lowercase letter, "
            + "one uppercase letter, and one special character") String passwordHash) {
  /**
   * Creates a simplified UserDtoRequest with only name.
   *
   * @param name
   *     the user's first name
   */
  public UserDtoRequest(String name) {
    this(null, name, null, null, null, null, null, null, null, null, null);
  }
}