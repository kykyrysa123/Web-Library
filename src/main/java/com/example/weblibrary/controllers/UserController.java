package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.UserDtoRequest;
import com.example.weblibrary.model.dto.UserDtoResponse;
import com.example.weblibrary.service.impl.UserServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing users.
 * This controller handles requests for getting, creating, updating, and deleting users.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserServiceImpl userService;

  /**
   * Retrieves a list of all users.
   *
   * @return A list of all users.
   */
  @GetMapping
  public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
    return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
  }

  /**
   * Retrieves a user by ID.
   *
   * @param id The ID of the user to retrieve.
   * @return The user with the given ID.
   */
  @GetMapping("/{id}")
  public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id) {
    return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
  }

  /**
   * Creates a new user.
   *
   * @param userDtoRequest The user data for creation.
   * @return The created user.
   */
  @PostMapping
  public ResponseEntity<UserDtoResponse> createUser(@RequestBody UserDtoRequest userDtoRequest) {
    return new ResponseEntity<>(userService.create(userDtoRequest), HttpStatus.CREATED);
  }

  /**
   * Updates an existing user.
   *
   * @param id              The ID of the user to update.
   * @param userDtoRequest The new user data.
   * @return The updated user.
   */
  @PutMapping("/{id}")
  public ResponseEntity<UserDtoResponse> updateUser(@PathVariable Long id,
      @RequestBody UserDtoRequest userDtoRequest) {
    return new ResponseEntity<>(userService.update(id, userDtoRequest), HttpStatus.OK);
  }

  /**
   * Deletes a user by ID.
   *
   * @param id The ID of the user to delete.
   * @return HTTP response with no content.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
