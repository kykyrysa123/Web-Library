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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {
  private final BookServiceImpl bookService;

  public BookController(BookServiceImpl bookService) {
    this.bookService = bookService;
  }

  @GetMapping
  public ResponseEntity<List<Book>> getAllBooks(@Nullable String genre
  ) {
    final List<Book> books;

    if (genre == null) {
      books = bookService.getAll();
    } else {
      books = bookService.getBooksByGenre(genre);
    }

    return new ResponseEntity<>(books, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Book> getBookById(@PathVariable int id) {
    return bookService.getById(id).map(
        book -> new ResponseEntity<>(book, HttpStatus.OK)).orElseGet(
        () -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  public ResponseEntity<Book> createBook(@RequestBody Book book) {
    Book createdBook = bookService.create(book);
    return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Book> updateBook(@PathVariable int id,
      @RequestBody Book book
  ) {
    Book updatedBook = bookService.update(id, book);
    return new ResponseEntity<>(updatedBook, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable int id) {
    bookService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
  //imp сделать отдельнйо папки

}