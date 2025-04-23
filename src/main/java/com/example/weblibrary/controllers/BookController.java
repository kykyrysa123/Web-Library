package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.service.impl.BookServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Управление книгами")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {
  private static final Logger logger = LoggerFactory.getLogger(BookController.class);
  private final BookServiceImpl bookService;

  @GetMapping
  @Operation(summary = "Получить список книг", description = "Возвращает список всех книг")
  public ResponseEntity<List<BookDtoResponse>> getBooks() {
    logger.info("Получен запрос на получение всех книг.");
    return ResponseEntity.ok(bookService.getAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить книгу по ID", description = "Возвращает книгу по её идентификатору")
  public ResponseEntity<BookDtoResponse> getBookById(@PathVariable Long id) {
    logger.info("Получен запрос на получение книги с ID: {}", id);
    try {
      return ResponseEntity.ok(bookService.getById(id));
    } catch (RuntimeException e) {
      logger.warn("Книга с ID {} не найдена: {}", id, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping("/genre")
  @Operation(summary = "Получить книги по жанру", description = "Возвращает список книг указанного жанра")
  public ResponseEntity<List<BookDtoResponse>> getBooksByGenre(
      @RequestParam @NotBlank(message = "Жанр не должен быть пустым") String genre) {
    logger.info("Получен запрос на получение книг по жанру: {}", genre);
    return ResponseEntity.ok(bookService.getByGenre(genre));
  }

  @GetMapping("/by-title")
  @Operation(summary = "Получить книги по названию", description = "Возвращает список книг, содержащих указанное название")
  public ResponseEntity<List<BookDtoResponse>> getBookByTitle(
      @RequestParam @NotBlank(message = "Название не должно быть пустым") String title) {
    logger.info("Получен запрос на поиск книг по названию: {}", title);
    return ResponseEntity.ok(bookService.getBookByTitle(title));
  }

  @PostMapping
  @Operation(summary = "Создать книгу", description = "Создаёт новую книгу и возвращает её данные")
  public ResponseEntity<BookDtoResponse> createBook(@Valid @RequestBody BookDtoRequest request) {
    logger.info("Получен запрос на создание новой книги с authorIds: {}", request.authorIds());
    try {
      BookDtoResponse createdBook = bookService.create(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    } catch (IllegalArgumentException e) {
      logger.error("Ошибка создания книги: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    } catch (RuntimeException e) {
      logger.error("Ошибка создания книги: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить данные книги", description = "Обновляет информацию о книге по её идентификатору")
  public ResponseEntity<BookDtoResponse> updateBook(
      @PathVariable Long id, @Valid @RequestBody BookDtoRequest request) {
    logger.info("Получен запрос на обновление книги с ID: {}", id);
    try {
      BookDtoResponse updatedBook = bookService.update(id, request);
      return ResponseEntity.ok(updatedBook);
    } catch (IllegalArgumentException e) {
      logger.error("Ошибка обновления книги: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    } catch (RuntimeException e) {
      logger.error("Ошибка обновления книги: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить книгу", description = "Удаляет книгу по её идентификатору")
  public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    logger.warn("Получен запрос на удаление книги с ID: {}", id);
    try {
      bookService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      logger.warn("Книга с ID {} не найдена: {}", id, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @PostMapping("/bulk")
  @Operation(summary = "Создать несколько книг", description = "Создаёт сразу несколько книг и возвращает их список")
  public ResponseEntity<List<BookDtoResponse>> createBooksBulk(
      @Valid @RequestBody List<BookDtoRequest> requests) {
    logger.info("Получен запрос на создание {} книг.", requests.size());
    try {
      List<BookDtoResponse> createdBooks = bookService.createBooksBulk(requests);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdBooks);
    } catch (IllegalArgumentException e) {
      logger.error("Ошибка создания книг: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    } catch (RuntimeException e) {
      logger.error("Ошибка создания книг: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    logger.warn("Ошибка валидации: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }
    return ResponseEntity.badRequest().body(errors);
  }
}