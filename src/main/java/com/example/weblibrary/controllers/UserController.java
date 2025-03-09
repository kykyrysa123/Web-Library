package com.example.weblibrary.controllers;

import com.example.weblibrary.model.User;
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
 * Controller to handle the CRUD operations for users in the web library system.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserServiceImpl userService;

  /**
   * Get all users.
   *
   * @return A list of all users.
   */
  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
  }

  /**
   * Get a user by its ID.
   *
   * @param id The ID of the user.
   * @return The user if found, otherwise a NOT_FOUND response.
   */
  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable int id) {
    return userService.getById(id)
                      .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * Create a new user.
   *
   * @param user The user to be created.
   * @return The created user.
   */
  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
  }

  /**
   * Update an existing user.
   *
   * @param id   The ID of the user to be updated.
   * @param user The user data to update.
   * @return The updated user.
   */
  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
    return new ResponseEntity<>(userService.update(id, user), HttpStatus.OK);
  }

  /**
   * Delete a user by its ID.
   *
   * @param id The ID of the user to be deleted.
   * @return A response indicating the deletion was successful.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable int id) {
    userService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
