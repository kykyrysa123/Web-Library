package com.example.weblibrary.model.dto;

import java.time.LocalDate;

/**
 * Data transfer object for responding with user information.
 * This record is used to send user data in responses.
 *
 * @param id the user's unique identifier
 * @param name the user's first name
 * @param surname the user's last name
 * @param patronymic the user's middle name (optional)
 * @param age the user's age
 * @param subscription the user's subscription type
 * @param sex the user's gender (MALE/FEMALE/OTHER)
 * @param country the user's country of residence
 * @param favoriteBooks the user's favorite books list
 * @param email the user's email address
 * @param passwordHash the hashed password (should not be exposed in production)
 * @param registrationDate the date when user registered
 * @param lastLogin the date of user's last login
 */
public record UserDtoResponse(
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
    String passwordHash,
    LocalDate registrationDate,
    LocalDate lastLogin
) {
  /**
   * Creates a simplified UserDtoResponse with only ID and name.
   *
   * @param id the user's unique identifier
   * @param name the user's first name
   */
  public UserDtoResponse(Long id, String name) {
    this(id, name, null, null, null, null, null, null, null, null, null, null, null);
  }

}