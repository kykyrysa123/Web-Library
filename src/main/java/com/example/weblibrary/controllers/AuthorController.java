package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.service.impl.AuthorServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления авторами.
 */
@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Управление авторами")
public class AuthorController {

  private final AuthorServiceImpl authorService;

  /**
   * Получает список всех авторов.
   *
   * @return ResponseEntity со списком всех авторов
   */
  @GetMapping
  @Operation(summary = "Получить всех авторов", description = "Возвращает "
      + "список всех авторов")
  public ResponseEntity<List<AuthorDtoResponse>> getAllAuthors() {
    return ResponseEntity.ok(authorService.getAll());
  }

  /**
   * Получает автора по его идентификатору.
   *
   * @param id
   *     идентификатор автора
   * @return ResponseEntity с данными автора
   * @throws EntityNotFoundException
   *     если автор не найден
   */
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

  /**
   * Получает автора вместе со списком его книг.
   *
   * @param id
   *     идентификатор автора
   * @return ResponseEntity с данными автора и списком его книг
   */
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

  /**
   * Создает нового автора.
   *
   * @param request
   *     DTO с данными для создания автора
   * @return ResponseEntity с данными созданного автора
   */
  @PostMapping
  @Operation(summary = "Создать нового автора", description = "Создаёт нового"
      + " автора и возвращает его данные")
  public ResponseEntity<AuthorDtoResponse> createAuthor(
      @Valid @RequestBody AuthorDtoRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(
        authorService.create(request));
  }

  /**
   * Обновляет данные автора.
   *
   * @param id
   *     идентификатор автора
   * @param request
   *     DTO с обновленными данными автора
   * @return ResponseEntity с обновленными данными автора
   */
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

  /**
   * Удаляет автора по его идентификатору.
   *
   * @param id
   *     идентификатор автора
   * @return ResponseEntity без содержимого
   */
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