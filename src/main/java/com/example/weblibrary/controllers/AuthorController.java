package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.service.impl.AuthorServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Управление авторами")
@SecurityRequirement(name = "Bearer Authentication")
public class AuthorController {

  private final AuthorServiceImpl authorService;

  @GetMapping
  @Operation(summary = "Получить всех авторов")
  public ResponseEntity<List<AuthorDtoResponse>> getAllAuthors() {
    return ResponseEntity.ok(authorService.getAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить автора по ID")
  public ResponseEntity<AuthorDtoResponse> getAuthorById(@PathVariable Long id) {
    return ResponseEntity.ok(authorService.getById(id));
  }

  @GetMapping("/{id}/with-books")
  @Operation(summary = "Получить автора с книгами")
  public ResponseEntity<AuthorDtoResponse> getAuthorWithBooks(@PathVariable Long id) {
    return ResponseEntity.ok(authorService.getAuthorWithBooks(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Создать нового автора (только ADMIN)")
  public ResponseEntity<AuthorDtoResponse> createAuthor(@Valid @RequestBody AuthorDtoRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authorService.create(request));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Обновить данные автора (только ADMIN)")
  public ResponseEntity<AuthorDtoResponse> updateAuthor(@PathVariable Long id,
                                                        @Valid @RequestBody AuthorDtoRequest request) {
    return ResponseEntity.ok(authorService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Удалить автора (только ADMIN)")
  public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
    authorService.delete(id);
    return ResponseEntity.noContent().build();
  }
}