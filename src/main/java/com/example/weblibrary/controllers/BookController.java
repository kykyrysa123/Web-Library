package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.service.impl.BookServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Управление книгами")
@SecurityRequirement(name = "Bearer Authentication")
public class BookController {

  private final BookServiceImpl bookService;

  @GetMapping
  @Operation(summary = "Получить список книг")
  public ResponseEntity<List<BookDtoResponse>> getBooks() {
    return ResponseEntity.ok(bookService.getAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить книгу по ID")
  public ResponseEntity<BookDtoResponse> getBookById(@PathVariable Long id) {
    return ResponseEntity.ok(bookService.getById(id));
  }

  @GetMapping("/genre")
  @Operation(summary = "Получить книги по жанру")
  public ResponseEntity<List<BookDtoResponse>> getBooksByGenre(@RequestParam String genre) {
    return ResponseEntity.ok(bookService.getByGenre(genre));
  }

  @GetMapping("/by-title")
  @Operation(summary = "Получить книги по названию")
  public ResponseEntity<List<BookDtoResponse>> getBookByTitle(@RequestParam String title) {
    return ResponseEntity.ok(bookService.getBookByTitle(title));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Создать книгу (только ADMIN)")
  public ResponseEntity<BookDtoResponse> createBook(@Valid @RequestBody BookDtoRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(request));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Обновить данные книги (только ADMIN)")
  public ResponseEntity<BookDtoResponse> updateBook(@PathVariable Long id,
                                                    @Valid @RequestBody BookDtoRequest request) {
    return ResponseEntity.ok(bookService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Удалить книгу (только ADMIN)")
  public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    bookService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/bulk")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Создать несколько книг (только ADMIN)")
  public ResponseEntity<List<BookDtoResponse>> createBooksBulk(@Valid @RequestBody List<BookDtoRequest> requests) {
    return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBooksBulk(requests));
  }
}