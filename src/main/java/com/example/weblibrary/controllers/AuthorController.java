package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.service.impl.AuthorServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления авторами.
 */
@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Управление авторами")
public class AuthorController {

  private final AuthorServiceImpl authorService;

  @GetMapping
  @Operation(summary = "Получить всех авторов", description = "Возвращает "
      + "список всех авторов")
  public ResponseEntity<List<AuthorDtoResponse>> getAllAuthors() {
    return ResponseEntity.ok(authorService.getAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить автора по ID", description = "Возвращает "
      + "автора по его идентификатору")
  public ResponseEntity<AuthorDtoResponse> getAuthorById(@PathVariable Long id
  ) {
    if (!authorService.existsById(id)) {
      throw new EntityNotFoundException("Автор с ID " + id + " не найден");
    }
    return ResponseEntity.ok(authorService.getById(id));
  }

  @GetMapping("/{id}/with-books")
  @Operation(summary = "Получить автора с книгами", description = "Возвращает"
      + " автора вместе со списком его книг")
  public ResponseEntity<AuthorDtoResponse> getAuthorWithBooks(
      @PathVariable Long id
  ) {
    try {
      AuthorDtoResponse author = authorService.getAuthorWithBooks(id);
      return ResponseEntity.ok(author);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @PostMapping
  @Operation(summary = "Создать нового автора", description = "Создаёт нового"
      + " автора и возвращает его данные")
  public ResponseEntity<AuthorDtoResponse> createAuthor(
      @Valid @RequestBody AuthorDtoRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(
        authorService.create(request));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить данные автора", description = "Обновляет "
      + "информацию об авторе по его идентификатору")
  public ResponseEntity<AuthorDtoResponse> updateAuthor(@PathVariable Long id,
      @Valid @RequestBody AuthorDtoRequest request
  ) {
    try {
      AuthorDtoResponse updatedAuthor = authorService.update(id, request);
      return ResponseEntity.ok(updatedAuthor);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить автора", description = "Удаляет автора по его"
      + " идентификатору")
  public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
    if (!authorService.existsById(id)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    authorService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
