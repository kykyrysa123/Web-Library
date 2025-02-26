package com.example.weblibrary.service;

import com.example.weblibrary.model.Book;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing book-related operations.
 * Defines methods for retrieving, creating, updating, and deleting books.
 */
public interface BookService {
  /**
   * Retrieves a list of all books.
   *
   * @return a list containing all books
   */
  List<Book> getAllBooks();

  /**
   * Retrieves a book by its ID.
   *
   * @param id the ID of the book to retrieve
   * @return an Optional containing the book if found, or empty if not
   */
  Optional<Book> getBookById(int id);

  /**
   * Creates a new book.
   *
   * @param book the book object to create
   * @return the created book
   */
  Book createBook(Book book);

  /**
   * Updates an existing book by its ID.
   *
   * @param id   the ID of the book to update
   * @param book the book object with updated data
   * @return the updated book
   */
  Book updateBook(int id, Book book);

  /**
   * Deletes a book by its ID.
   *
   * @param id the ID of the book to delete
   */
  void deleteBook(int id);
}