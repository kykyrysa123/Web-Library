package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.service.impl.AuthorServiceImpl;
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
 * Controller for managing author-related operations. Provides endpoints to
 * retrieve, create, update, and delete authors.
 */
@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

  private final AuthorServiceImpl authorService;

  /**
   * Retrieves a list of all authors.
   *
   * @return ResponseEntity containing a list of AuthorDtoResponse objects and HTTP status OK.
   */
  @GetMapping
  public ResponseEntity<List<AuthorDtoResponse>> getAllAuthors() {
    return new ResponseEntity<>(authorService.getAll(), HttpStatus.OK);
  }

  /**
   * Retrieves an author by their ID.
   *
   * @param id the ID of the author to retrieve.
   * @return ResponseEntity containing the AuthorDtoResponse object and HTTP status OK.
   */
  @GetMapping("/{id}")
  public ResponseEntity<AuthorDtoResponse> getAuthorById(@PathVariable Long id) {
    return new ResponseEntity<>(authorService.getById(id), HttpStatus.OK);
  }

  /**
   * Retrieves an author along with their associated books by the author's ID.
   *
   * @param id the ID of the author to retrieve.
   * @return ResponseEntity containing the AuthorDtoResponse object and HTTP status OK.
   */
  @GetMapping("/{id}/with-books")
  public ResponseEntity<AuthorDtoResponse> getAuthorWithBooks(@PathVariable Long id) {
    return new ResponseEntity<>(authorService.getAuthorWithBooks(id), HttpStatus.OK);
  }

  /**
   * Creates a new author.
   *
   * @param authorDtoRequest the AuthorDtoRequest object containing author details.
   * @return ResponseEntity containing the created AuthorDtoResponse object and HTTP status CREATED.
   */
  @PostMapping
  public ResponseEntity<AuthorDtoResponse> createAuthor(
      @RequestBody AuthorDtoRequest authorDtoRequest) {
    return new ResponseEntity<>(authorService.create(
        authorDtoRequest
    ),
        HttpStatus.CREATED
    );
  }

  /**
   * Updates an existing author by their ID.
   *
   * @param id the ID of the author to update.
   * @param authorDtoRequest the AuthorDtoRequest object containing updated author details.
   * @return ResponseEntity containing the updated AuthorDtoResponse object and HTTP status OK.
   */
  @PutMapping("/{id}")
  public ResponseEntity<AuthorDtoResponse> updateAuthor(@PathVariable Long id,
      @RequestBody AuthorDtoRequest authorDtoRequest) {
    return new ResponseEntity<>(authorService.update(id, authorDtoRequest), HttpStatus.OK);
  }

  /**
   * Deletes an author by their ID.
   *
   * @param id the ID of the author to delete.
   * @return ResponseEntity with HTTP status NO_CONTENT.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
    authorService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
