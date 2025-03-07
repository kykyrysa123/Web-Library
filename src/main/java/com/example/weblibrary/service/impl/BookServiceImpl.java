package com.example.weblibrary.service.impl;

import com.example.weblibrary.model.Book;
import com.example.weblibrary.repository.BookRepository;

import java.util.List;
import java.util.Optional;

import com.example.weblibrary.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing book-related operations. Provides methods
 * to retrieve, create, update, and delete books using the BookRepository.
 */
@Service
public class BookServiceImpl implements CrudService<Book> {
  private final BookRepository bookRepository;

  /**
   * Constructs a new BookServiceImpl with the specified BookRepository.
   *
   * @param bookRepository
   *     the repository used for book data operations
   */
  @Autowired
  public BookServiceImpl(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public List<Book> getAll() {
    return bookRepository.findAll();
  }

  @Override
  public Optional<Book> getById(int id) {
    return bookRepository.findById(id);
  }

  @Override
  public Book create(Book book) {

    return bookRepository.save(book);
  }

  @Override
  public Book update(int id, Book bookDetails) {
    Book book = bookRepository.findById(id).orElseThrow(
        () -> new RuntimeException("Book not found with id: " + id));
    book.setTitle(bookDetails.getTitle());
    book.setPublisher(bookDetails.getPublisher());
    book.setIsbn(bookDetails.getIsbn());
    book.setPages(bookDetails.getPages());
    book.setGenre(bookDetails.getGenre());
    book.setPublishDate(bookDetails.getPublishDate());
    book.setLanguage(bookDetails.getLanguage());
    book.setDescription(bookDetails.getDescription());
    book.setImageUrl(bookDetails.getImageUrl());
    return bookRepository.save(book);
  }

  @Override
  public void delete(int id) {
    Book book = bookRepository.findById(id).orElseThrow(
        () -> new RuntimeException("Book not found with id: " + id));
    bookRepository.delete(book);
  }

  /**
   * Retrieves a list of books filtered by genre.
   *
   * @param genre
   *     the genre to filter books by
   * @return a list of books matching the specified genre
   */
  public List<Book> getBooksByGenre(String genre) {
    return bookRepository.findByGenre(genre);
  }
}