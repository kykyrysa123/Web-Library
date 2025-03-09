package com.example.weblibrary.controllers;

import com.example.weblibrary.model.Author;
import com.example.weblibrary.service.impl.AuthorServiceImpl;
import java.util.List;
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
 * Controller for managing authors.
 */
@RestController
@RequestMapping("/api/authors")
public class AuthorController {
  private final AuthorServiceImpl authorService;

  /**
   * Constructor for AuthorController.
   *
   * @param authorService service for managing authors
   */
  public AuthorController(AuthorServiceImpl authorService) {
    this.authorService = authorService;
  }

  /**
   * Retrieve all authors.
   *
   * @return list of authors
   */
  @GetMapping
  public ResponseEntity<List<Author>> getAllAuthors() {
    return new ResponseEntity<>(authorService.getAll(), HttpStatus.OK);
  }

  /**
   * Retrieve an author by ID.
   *
   * @param id author ID
   * @return author or 404 if not found
   */
  @GetMapping("/{id}")
  public ResponseEntity<Author> getAuthorById(@PathVariable int id) {
    return authorService.getById(id)
                        .map(author -> new ResponseEntity<>(author, HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * Create a new author.
   *
   * @param author author object
   * @return created author
   */
  @PostMapping
  public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
    return new ResponseEntity<>(authorService.create(author), HttpStatus.CREATED);
  }

  /**
   * Update an existing author.
   *
   * @param id author ID
   * @param author updated author data
   * @return updated author
   */
  @PutMapping("/{id}")
  public ResponseEntity<Author> updateAuthor(@PathVariable int id, @RequestBody Author author) {
    return new ResponseEntity<>(authorService.update(id, author), HttpStatus.OK);
  }

  /**
   * Delete an author.
   *
   * @param id author ID
   * @return operation status
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAuthor(@PathVariable int id) {
    authorService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
