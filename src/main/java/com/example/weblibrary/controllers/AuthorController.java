package com.example.weblibrary.controllers;

import com.example.weblibrary.model.Author;
import com.example.weblibrary.service.impl.AuthorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
//@RequiredArgsConstructor
public class AuthorController {
  private final AuthorServiceImpl authorService;

  public AuthorController(AuthorServiceImpl authorService) {
    this.authorService = authorService;
  }

  @GetMapping
  public ResponseEntity<List<Author>> getAllAuthors() {
    return new ResponseEntity<>(authorService.getAll(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Author> getAuthorById(@PathVariable int id) {
    return authorService.getById(id).map(
        author -> new ResponseEntity<>(author, HttpStatus.OK)).orElseGet(
        () -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
    return new ResponseEntity<>(authorService.create(author),
        HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Author> updateAuthor(@PathVariable int id,
      @RequestBody Author author
  ) {
    return new ResponseEntity<>(authorService.update(id, author),
        HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAuthor(@PathVariable int id) {
    authorService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
