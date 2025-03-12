package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.service.impl.BookServiceImpl;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing book-related operations. Provides endpoints to
 * retrieve, create, update, and delete books.
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

  private final BookServiceImpl bookService;

  /**
   * Retrieves a list of all books.
   *
   * @return ResponseEntity containing a list of BookDtoResponse objects and HTTP status OK.
   */
  @GetMapping
  public ResponseEntity<List<BookDtoResponse>> getAllBooks() {
    return new ResponseEntity<>(bookService.getAll(), HttpStatus.OK);
  }

  /**
   * Retrieves a book by its ID.
   *
   * @param id the ID of the book to retrieve.
   * @return ResponseEntity containing the BookDtoResponse object and HTTP status OK.
   */
  @GetMapping("/{id}")
  public ResponseEntity<BookDtoResponse> getBookById(@PathVariable Long id) {
    return new ResponseEntity<>(bookService.getById(id), HttpStatus.OK);
  }

  /**
   * Creates a new book.
   *
   * @param bookDtoRequest the BookDtoRequest object containing book details.
   * @return ResponseEntity containing the created BookDtoResponse object and HTTP status CREATED.
   */
  @PostMapping
  public ResponseEntity<BookDtoResponse> createBook(@RequestBody BookDtoRequest bookDtoRequest) {
    return new ResponseEntity<>(bookService.create(bookDtoRequest), HttpStatus.CREATED);
  }

  /**
   * Updates an existing book by its ID.
   *
   * @param id the ID of the book to update.
   * @param bookDtoRequest the BookDtoRequest object containing updated book details.
   * @return ResponseEntity containing the updated BookDtoResponse object and HTTP status OK.
   */
  @PutMapping("/{id}")
  public ResponseEntity<BookDtoResponse> updateBook(
      @PathVariable Long id,
      @RequestBody BookDtoRequest bookDtoRequest
  ) {
    return new ResponseEntity<>(bookService.update(id, bookDtoRequest), HttpStatus.OK);
  }

  /**
   * Deletes a book by its ID.
   *
   * @param id the ID of the book to delete.
   * @return ResponseEntity with HTTP status NO_CONTENT.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    bookService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Retrieves a list of books by genre.
   *
   * @param genre the genre to filter books by.
   * @return ResponseEntity containing a list of BookDtoResponse objects and HTTP status OK.
   */
  @GetMapping("/genre")
  public ResponseEntity<List<BookDtoResponse>> getBooksByGenre(@RequestParam String genre) {
    return new ResponseEntity<>(bookService.getByGenre(genre), HttpStatus.OK);
  }
}