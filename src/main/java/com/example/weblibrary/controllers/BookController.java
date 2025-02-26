package com.example.weblibrary.controllers;

import com.example.weblibrary.model.Book;
import com.example.weblibrary.service.BookServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing books in the web library application.
 * Provides endpoints to retrieve, create, update, and delete books.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {
  private final BookServiceImpl bookService;

  /**
   * Constructs a new BookController with the specified BookServiceImpl.
   *
   * @param bookService the service implementation for book operations
   */
  @Autowired
  public BookController(BookServiceImpl bookService) {
    this.bookService = bookService;
  }

  /**
   * Retrieves a list of all books.
   *
   * @return a ResponseEntity containing the list of books and HTTP status OK
   */
  @GetMapping
  public ResponseEntity<List<Book>> getAllBooks() {
    List<Book> books = bookService.getAllBooks();
    return new ResponseEntity<>(books, HttpStatus.OK);
  }

  /**
   * Searches for books by title using query parameters.
   *
   * @param title the title substring to search for (case-insensitive)
   * @return a ResponseEntity containing the filtered list of books and HTTP status OK
   */
  @GetMapping("/search")
  public ResponseEntity<List<Book>> getBooksByQueryParams(@RequestParam String title) {
    List<Book> books = bookService.getAllBooks().stream().filter(
        book -> book.getTitle().toLowerCase().contains(title.toLowerCase())).toList();
    return new ResponseEntity<>(books, HttpStatus.OK);
  }

  /**
   * Retrieves a book by its ID.
   *
   * @param id the ID of the book to retrieve
   * @return a ResponseEntity containing the book and HTTP status OK, or NOT_FOUND if not found
   */
  @GetMapping("/{id}")
  public ResponseEntity<Book> getBookById(@PathVariable int id) {
    return bookService.getBookById(id)
                      .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * Creates a new book.
   *
   * @param book the book object to create
   * @return a ResponseEntity containing the created book and HTTP status CREATED
   */
  @PostMapping
  public ResponseEntity<Book> createBook(@RequestBody Book book) {
    Book createdBook = bookService.createBook(book);
    return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
  }

  /**
   * Updates an existing book by its ID.
   *
   * @param id   the ID of the book to update
   * @param book the updated book object
   * @return a ResponseEntity containing the updated book and HTTP status OK
   */
  @PutMapping("/{id}")
  public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book book) {
    Book updatedBook = bookService.updateBook(id, book);
    return new ResponseEntity<>(updatedBook, HttpStatus.OK);
  }

  /**
   * Deletes a book by its ID.
   *
   * @param id the ID of the book to delete
   * @return a ResponseEntity with HTTP status NO_CONTENT
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable int id) {
    bookService.deleteBook(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Retrieves a list of books by genre.
   *
   * @param genre the genre to filter books by
   * @return a ResponseEntity containing the list of books and HTTP status OK
   */
  @GetMapping("/genre")
  public ResponseEntity<List<Book>> getBookByGenre(@RequestParam String genre) {
    return new ResponseEntity<>(bookService.getBooksByGenre(genre), HttpStatus.OK);
  }
}