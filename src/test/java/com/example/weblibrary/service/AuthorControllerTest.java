package com.example.weblibrary.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.weblibrary.controllers.AuthorController;
import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.service.impl.AuthorServiceImpl;
import jakarta.persistence.EntityNotFoundException;
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
class AuthorControllerTest {

  @InjectMocks
  private AuthorController authorController;

  @Mock
  private AuthorServiceImpl authorService;

  private AuthorDtoRequest authorRequest;
  private AuthorDtoResponse authorResponse;

  @BeforeEach
  void setUp() {
    authorRequest = new AuthorDtoRequest("John", "Doe", null, LocalDate.of(1980, 1, 1), null, "Bio", "Fiction", 4.5);
    authorResponse = new AuthorDtoResponse(1L, "John", "Doe", null, LocalDate.of(1980, 1, 1), null, "Bio", "Fiction", 4.5, null);
  }

  @Test
  void testGetAllAuthors_Success() {
    List<AuthorDtoResponse> authors = List.of(authorResponse);
    when(authorService.getAll()).thenReturn(authors);

    ResponseEntity<List<AuthorDtoResponse>> response = authorController.getAllAuthors();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(authors, response.getBody());
  }

  @Test
  void testGetAuthorById_Success() {
    Long id = 1L;
    when(authorService.existsById(id)).thenReturn(true);
    when(authorService.getById(id)).thenReturn(authorResponse);

    ResponseEntity<AuthorDtoResponse> response = authorController.getAuthorById(id);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(authorResponse, response.getBody());
  }

  @Test
  void testGetAuthorById_NotFound() {
    Long id = 1L;
    when(authorService.existsById(id)).thenReturn(false);

    EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
        () -> authorController.getAuthorById(id));
    assertEquals("Автор с ID " + id + " не найден", exception.getMessage());
  }

  @Test
  void testGetAuthorWithBooks_Success() {
    Long id = 1L;
    when(authorService.getAuthorWithBooks(id)).thenReturn(authorResponse);

    ResponseEntity<AuthorDtoResponse> response = authorController.getAuthorWithBooks(id);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(authorResponse, response.getBody());
  }

  @Test
  void testGetAuthorWithBooks_NotFound() {
    Long id = 1L;
    when(authorService.getAuthorWithBooks(id)).thenThrow(new EntityNotFoundException("Author not found"));

    ResponseEntity<AuthorDtoResponse> response = authorController.getAuthorWithBooks(id);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void testCreateAuthor_Success() {
    when(authorService.create(authorRequest)).thenReturn(authorResponse);

    ResponseEntity<AuthorDtoResponse> response = authorController.createAuthor(authorRequest);

    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(authorResponse, response.getBody());
  }

  @Test
  void testUpdateAuthor_Success() {
    Long id = 1L;
    when(authorService.update(id, authorRequest)).thenReturn(authorResponse);

    ResponseEntity<AuthorDtoResponse> response = authorController.updateAuthor(id, authorRequest);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(authorResponse, response.getBody());
  }

  @Test
  void testUpdateAuthor_NotFound() {
    Long id = 1L;
    when(authorService.update(id, authorRequest)).thenThrow(new EntityNotFoundException("Author not found"));

    ResponseEntity<AuthorDtoResponse> response = authorController.updateAuthor(id, authorRequest);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void testDeleteAuthor_Success() {
    Long id = 1L;
    when(authorService.existsById(id)).thenReturn(true);
    doNothing().when(authorService).delete(id);

    ResponseEntity<Void> response = authorController.deleteAuthor(id);

    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void testDeleteAuthor_NotFound() {
    Long id = 1L;
    when(authorService.existsById(id)).thenReturn(false);

    ResponseEntity<Void> response = authorController.deleteAuthor(id);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }
}