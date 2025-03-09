package com.example.weblibrary.controllers;

import com.example.weblibrary.model.Book;
import com.example.weblibrary.service.impl.BookServiceImpl;
import jakarta.annotation.Nullable;
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
 * Controller for handling book-related API requests.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

  private final BookServiceImpl bookService;

  /**
   * Constructs a BookController with the given BookService.
   *
   * @param bookService The book service to be used by the controller.
   */
  public BookController(BookServiceImpl bookService) {
    this.bookService = bookService;
  }

  /**
   * Retrieves all books or filters books by genre if provided.
   *
   * @param genre Optional genre to filter books.
   * @return A list of books with HTTP status OK.
   */
  @GetMapping
  public ResponseEntity<List<Book>> getAllBooks(@Nullable String genre) {
    final List<Book> books;

    if (genre == null) {
      books = bookService.getAll();
    } else {
      books = bookService.getBooksByGenre(genre);
    }

    return new ResponseEntity<>(books, HttpStatus.OK);
  }

  /**
   * Retrieves a book by its ID.
   *
   * @param id The ID of the book.
   * @return The book with HTTP status OK, or a 404 status if not found.
   */
  @GetMapping("/{id}")
  public ResponseEntity<Book> getBookById(@PathVariable int id) {
    return bookService.getById(id)
                      .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * Creates a new book.
   *
   * @param book The book to be created.
   * @return The created book with HTTP status CREATED.
   */
  @PostMapping
  public ResponseEntity<Book> createBook(@RequestBody Book book) {
    Book createdBook = bookService.create(book);
    return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
  }

  /**
   * Updates an existing book.
   *
   * @param id The ID of the book to be updated.
   * @param book The updated book data.
   * @return The updated book with HTTP status OK.
   */
  @PutMapping("/{id}")
  public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book book) {
    Book updatedBook = bookService.update(id, book);
    return new ResponseEntity<>(updatedBook, HttpStatus.OK);
  }

  /**
   * Deletes a book by its ID.
   *
   * @param id The ID of the book to be deleted.
   * @return HTTP status NO_CONTENT if the deletion was successful.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable int id) {
    bookService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
