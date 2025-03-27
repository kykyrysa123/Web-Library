package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.service.impl.BookServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления книгами.
 */
@Validated
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Управление книгами")
public class BookController {
  private static final Logger logger = LoggerFactory.getLogger(BookController.class);
  private final BookServiceImpl bookService;

  @GetMapping
  @Operation(summary = "Получить список книг", description = "Возвращает список всех книг или фильтрует по имени автора")
  public ResponseEntity<List<BookDtoResponse>> getBooks(
      @RequestParam(required = false) String authorName) {
    // Log a sanitized message, without the actual user input
    if (authorName != null) {
      logger.info("Получен запрос на получение книг с фильтром по автору.");
    } else {
      logger.info("Получен запрос на получение всех книг.");
    }

    if (StringUtils.hasText(authorName)) {
      return ResponseEntity.ok(bookService.getBooksByAuthorName(authorName));
    }
    return ResponseEntity.ok(bookService.getAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить книгу по ID", description = "Возвращает книгу по её идентификатору")
  public ResponseEntity<BookDtoResponse> getBookById(@PathVariable Long id) {
    logger.info("Получен запрос на получение книги с ID: {}", id);
    return ResponseEntity.ok(bookService.getById(id));
  }

  @GetMapping("/genre")
  @Operation(summary = "Получить книги по жанру", description = "Возвращает список книг указанного жанра")
  public ResponseEntity<List<BookDtoResponse>> getBooksByGenre(
      @RequestParam @NotBlank String genre) {

    // Log without exposing user-controlled data
    logger.info("Получен запрос на получение книг по жанру.");

    return ResponseEntity.ok(bookService.getByGenre(genre));
  }

  @GetMapping("/by-title")
  @Operation(summary = "Получить книги по названию", description = "Возвращает список книг, содержащих указанное название")
  public ResponseEntity<List<BookDtoResponse>> getBookByTitle(
      @RequestParam @NotBlank String title) {

    // Log without exposing user-controlled data
    logger.info("Получен запрос на поиск книг по названию.");

    return ResponseEntity.ok(bookService.getBookByTitle(title));
  }


  @PostMapping
  @Operation(summary = "Создать книгу", description = "Создаёт новую книгу и возвращает её данные")
  public ResponseEntity<BookDtoResponse> createBook(@Valid @RequestBody BookDtoRequest request) {
    logger.info("Получен запрос на создание новой книги: {}", request);
    return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(request));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить данные книги", description = "Обновляет информацию о книге по её идентификатору")
  public ResponseEntity<BookDtoResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookDtoRequest request) {
    logger.info("Получен запрос на обновление книги с ID: {}", id);
    return ResponseEntity.ok(bookService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить книгу", description = "Удаляет книгу по её идентификатору")
  public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    logger.warn("Получен запрос на удаление книги с ID: {}", id);
    bookService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/bulk")
  @Operation(summary = "Создать несколько книг", description = "Создаёт сразу несколько книг и возвращает их список")
  public ResponseEntity<List<BookDtoResponse>> createBooksBulk(@Valid @RequestBody List<BookDtoRequest> requests) {
    return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBooksBulk(requests));
  }


}
