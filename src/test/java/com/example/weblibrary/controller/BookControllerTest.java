package com.example.weblibrary.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.weblibrary.controllers.BookController;
import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

  @InjectMocks
  private BookController bookController;

  @Mock
  private BookServiceImpl bookService;

  private BookDtoRequest bookRequest;
  private BookDtoResponse bookResponse;

  @BeforeEach
  void setUp() {
    bookRequest = new BookDtoRequest("Test Book", "Test Publisher",
        "1234567890", 300, "Fiction", LocalDate.of(2020, 1, 1), "English",
        "A great book", "example.com/image.jpg", 4.5, 1L);
    bookResponse = new BookDtoResponse(1L, "Test Book", "Test Publisher",
        "1234567890", 300L, "Fiction", LocalDate.of(2020, 1, 1), "English",
        "A great book", "example.com/image.jpg");
  }

  @Test
  void testGetBooks_AllBooks() {
    List<BookDtoResponse> books = List.of(bookResponse);
    when(bookService.getAll()).thenReturn(books);

    ResponseEntity<List<BookDtoResponse>> response = bookController.getBooks(null);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(books, response.getBody());
  }

  @Test
  void testGetBooks_ByAuthorName() {
    String authorName = "John Doe";
    List<BookDtoResponse> books = List.of(bookResponse);
    when(bookService.getBooksByAuthorName(authorName)).thenReturn(books);

    ResponseEntity<List<BookDtoResponse>> response = bookController.getBooks(authorName);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(books, response.getBody());
  }

  @Test
  void testGetBookById_Success() {
    Long id = 1L;
    when(bookService.getById(id)).thenReturn(bookResponse);

    ResponseEntity<BookDtoResponse> response = bookController.getBookById(id);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(bookResponse, response.getBody());
  }

  @Test
  void testGetBooksByGenre_Success() {
    String genre = "Fiction";
    List<BookDtoResponse> books = List.of(bookResponse);
    when(bookService.getByGenre(genre)).thenReturn(books);

    ResponseEntity<List<BookDtoResponse>> response = bookController.getBooksByGenre(genre);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(books, response.getBody());
  }

  @Test
  void testGetBookByTitle_Success() {
    String title = "Test Book";
    List<BookDtoResponse> books = List.of(bookResponse);
    when(bookService.getBookByTitle(title)).thenReturn(books);

    ResponseEntity<List<BookDtoResponse>> response = bookController.getBookByTitle(title);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(books, response.getBody());
  }

  @Test
  void testCreateBook_Success() {
    when(bookService.create(bookRequest)).thenReturn(bookResponse);

    ResponseEntity<BookDtoResponse> response = bookController.createBook(bookRequest);

    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(bookResponse, response.getBody());
  }

  @Test
  void testUpdateBook_Success() {
    Long id = 1L;
    when(bookService.update(id, bookRequest)).thenReturn(bookResponse);

    ResponseEntity<BookDtoResponse> response = bookController.updateBook(id, bookRequest);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(bookResponse, response.getBody());
  }

  @Test
  void testDeleteBook_Success() {
    Long id = 1L;
    doNothing().when(bookService).delete(id);

    ResponseEntity<Void> response = bookController.deleteBook(id);

    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void testCreateBooksBulk_Success() {
    List<BookDtoRequest> requests = List.of(bookRequest);
    List<BookDtoResponse> responses = List.of(bookResponse);
    when(bookService.createBooksBulk(requests)).thenReturn(responses);

    ResponseEntity<List<BookDtoResponse>> response = bookController.createBooksBulk(requests);

    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(responses, response.getBody());
  }
}