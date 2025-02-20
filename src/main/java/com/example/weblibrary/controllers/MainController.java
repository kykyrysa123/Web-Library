package com.example.weblibrary.controllers;

import com.example.weblibrary.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling main page requests and user-related operations.
 */
@RestController
public class MainController {

  /**
   * Retrieves user information based on path variables.
   *
   * @param id
   *     the user's ID
   * @param login
   *     the user's login name
   * @param password
   *     the user's password (Note: Including passwords in path variables is
   *     insecure)
   * @return a {@link ResponseEntity} with the user's ID, login, and password
   */
  @GetMapping("/users/{id}/{login}/{password}")
  public ResponseEntity<UserDto> getUserPathVariable(
      @PathVariable final Long id, @PathVariable final String login,
      @PathVariable final String password
  ) {
    UserDto userDto = new UserDto(id, login, password);
    return ResponseEntity.ok(userDto);
  }

  /**
   * Retrieves user information based on query parameters.
   *
   * @param id
   *     the user's ID
   * @param login
   *     the user's login name
   * @param password
   *     the user's password (Note: Handling passwords in query parameters is
   *     not secure)
   * @return a {@link ResponseEntity} with the user's ID, login, and password
   */
  @GetMapping("/users")
  public ResponseEntity<UserDto> getUserQueryVariable(
      @RequestParam final Long id, @RequestParam final String login,
      @RequestParam final String password
  ) {
    return ResponseEntity.ok(new UserDto(id, login, password));
  }
}
