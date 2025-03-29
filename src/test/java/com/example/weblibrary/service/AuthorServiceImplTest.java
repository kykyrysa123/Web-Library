package com.example.weblibrary.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.weblibrary.mapper.AuthorMapper;
import com.example.weblibrary.mapper.AuthorMapperImpl;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.repository.AuthorRepository;
import com.example.weblibrary.service.cache.SimpleCache;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.weblibrary.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

  @Mock
  private AuthorRepository authorRepository;

  @Mock
  private AuthorMapperImpl authorMapper;

  @Mock
  private SimpleCache<Long, AuthorDtoResponse> authorCache;

  @Mock
  private SimpleCache<String, List<AuthorDtoResponse>> authorCache1;

  @InjectMocks
  private AuthorServiceImpl authorService;

  private Author author;
  private AuthorDtoRequest authorDtoRequest;
  private AuthorDtoResponse authorDtoResponse;

  @BeforeEach
  void setUp() {
    author = new Author(1L, "John", "Doe", null, LocalDate.of(1980, 1, 1), null, "Bio", "Fiction", 4.5, null);
    authorDtoRequest = new AuthorDtoRequest("John", "Doe", null, LocalDate.of(1980, 1, 1), null, "Bio", "Fiction", 4.5);
    authorDtoResponse = new AuthorDtoResponse(1L, "John", "Doe", null, LocalDate.of(1980, 1, 1), null, "Bio", "Fiction", 4.5, null);
  }

  @Test
  void testGetAll() {
    lenient().when(authorCache1.get("all_authors")).thenReturn(null);
    when(authorRepository.findAll()).thenReturn(List.of(author));
    when(authorMapper.toAuthorDtoResponse(List.of(author))).thenReturn(List.of(authorDtoResponse));

    List<AuthorDtoResponse> result = authorService.getAll();

    assertEquals(1, result.size());
    assertEquals("John", result.get(0).name());
  }

  @Test
  void testGetById() {
    lenient().when(authorCache.get(1L)).thenReturn(null);
    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    AuthorDtoResponse result = authorService.getById(1L);

    assertNotNull(result);
    assertEquals("John", result.name());
  }

  @Test
  void testGetById_NotFound() {
    when(authorRepository.findById(2L)).thenReturn(Optional.empty());
    Exception exception = assertThrows(RuntimeException.class, () -> authorService.getById(2L));
    assertTrue(exception.getMessage().contains("Author not found"));
  }

  @Test
  void testCreate() {
    when(authorMapper.toAuthorEntity(authorDtoRequest)).thenReturn(author);
    when(authorRepository.save(author)).thenReturn(author);
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    AuthorDtoResponse result = authorService.create(authorDtoRequest);

    assertNotNull(result);
    assertEquals("John", result.name());
  }

  @Test
  void testUpdate() {
    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
    when(authorMapper.toAuthorEntity(authorDtoRequest)).thenReturn(author);
    when(authorRepository.save(author)).thenReturn(author);
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    AuthorDtoResponse result = authorService.update(1L, authorDtoRequest);

    assertNotNull(result);
    assertEquals("John", result.name());
  }

  @Test
  void testUpdate_NotFound() {
    when(authorRepository.findById(2L)).thenReturn(Optional.empty());
    Exception exception = assertThrows(RuntimeException.class, () -> authorService.update(2L, authorDtoRequest));
    assertTrue(exception.getMessage().contains("Author not found"));
  }

  @Test
  void testDelete() {
    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
    doNothing().when(authorRepository).delete(author);

    assertDoesNotThrow(() -> authorService.delete(1L));
  }

  @Test
  void testDelete_NotFound() {
    when(authorRepository.findById(2L)).thenReturn(Optional.empty());
    Exception exception = assertThrows(RuntimeException.class, () -> authorService.delete(2L));
    assertTrue(exception.getMessage().contains("Author not found"));
  }
}