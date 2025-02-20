package com.example.weblibrary.dto;

/**
 * Data Transfer Object for User entity. This class is not designed to be
 * extended.
 *
 * @param id
 *     The unique identifier for the user.
 * @param name
 *     The first name of the user.
 * @param password
 *     The last name of the user.
 */
public record UserDto(Long id, String name, String password) {
}
