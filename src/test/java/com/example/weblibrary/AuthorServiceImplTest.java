package com.example.weblibrary;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.weblibrary.mapper.AuthorMapper;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.repository.AuthorRepository;
import com.example.weblibrary.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

  @Mock
  private AuthorRepository authorRepository;

  @Mock
  private AuthorMapper authorMapper;

  @InjectMocks
  private AuthorServiceImpl authorService;

  private Author author;
  private AuthorDtoRequest authorDtoRequest;
  private AuthorDtoResponse authorDtoResponse;

  @BeforeEach
  void setUp() {
    author = new Author(1L, "F. Scott", "Fitzgerald", null, null, null, null, null, 4.5, List.of());
    authorDtoRequest = new AuthorDtoRequest("F. Scott", "Fitzgerald", null, null, null, null, null, 4.5);
    authorDtoResponse = new AuthorDtoResponse(1L, "F. Scott", "Fitzgerald", null, null, null, null, null, 4.5);
  }

  @Test
  void getAllAuthors_ShouldReturnListOfAuthors() {
    when(authorRepository.findAll()).thenReturn(List.of(author));
    when(authorMapper.toAuthorDtoResponse(List.of(author))).thenReturn(List.of(authorDtoResponse));

    List<AuthorDtoResponse> result = authorService.getAll();

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals(authorDtoResponse.name(), result.get(0).name());
  }

  @Test
  void getAuthorById_ShouldReturnAuthor() {
    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    AuthorDtoResponse result = authorService.getById(1L);

    assertNotNull(result);
    assertEquals(authorDtoResponse.name(), result.name());
  }

  @Test
  void getAuthorById_ShouldThrowException_WhenNotFound() {
    when(authorRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(RuntimeException.class, () -> authorService.getById(1L));
  }

  @Test
  void createAuthor_ShouldReturnSavedAuthor() {
    when(authorMapper.toAuthorEntity(authorDtoRequest)).thenReturn(author);
    when(authorRepository.save(any(Author.class))).thenReturn(author);
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    AuthorDtoResponse result = authorService.create(authorDtoRequest);

    assertNotNull(result);
    assertEquals(authorDtoResponse.name(), result.name());
  }

  @Test
  void updateAuthor_ShouldReturnUpdatedAuthor() {
    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
    when(authorMapper.toAuthorEntity(authorDtoRequest)).thenReturn(author);
    when(authorRepository.save(any(Author.class))).thenReturn(author);
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    AuthorDtoResponse result = authorService.update(1L, authorDtoRequest);

    assertNotNull(result);
    assertEquals(authorDtoResponse.name(), result.name());
  }

  @Test
  void deleteAuthor_ShouldCallRepositoryDelete() {
    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
    doNothing().when(authorRepository).delete(author);

    assertDoesNotThrow(() -> authorService.delete(1L));
    verify(authorRepository, times(1)).delete(author);
  }
}
