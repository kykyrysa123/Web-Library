package com.example.weblibrary.service;

import com.example.weblibrary.mapper.AuthorMapperImpl;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.repository.AuthorRepository;
import com.example.weblibrary.service.cache.SimpleCache;
import com.example.weblibrary.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

  @Mock
  private AuthorRepository authorRepository;

  @Mock
  private AuthorMapperImpl authorMapper;

  @Mock
  private SimpleCache<Long, AuthorDtoResponse> authorCache;

  @Mock
  private SimpleCache<String, List<AuthorDtoResponse>> authorListCache;

  @InjectMocks
  private AuthorServiceImpl authorService;

  private Author author;
  private AuthorDtoRequest authorDtoRequest;
  private AuthorDtoResponse authorDtoResponse;
  private final Logger logger = LoggerFactory.getLogger(AuthorServiceImplTest.class);

  @BeforeEach
  void setUp() {
    author = new Author();
    author.setId(1L);
    author.setName("J.R.R. Tolkien");

    authorDtoRequest = new AuthorDtoRequest("J.R.R. Tolkien");
    authorDtoResponse = new AuthorDtoResponse(1L, "J.R.R. Tolkien");
  }

  @Test
  void getAll_ShouldReturnAuthorsFromCache() {
    // Arrange
    String cacheKey = "all_authors";
    when(authorListCache.get(cacheKey)).thenReturn(List.of(authorDtoResponse));

    // Act
    List<AuthorDtoResponse> result = authorService.getAll();

    // Assert
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(authorDtoResponse);
    verify(authorListCache).get(cacheKey);
    verifyNoInteractions(authorRepository);
  }

  @Test
  void getAll_ShouldFetchFromDBAndCacheWhenCacheEmpty() {
    // Arrange
    String cacheKey = "all_authors";
    when(authorListCache.get(cacheKey)).thenReturn(null);
    when(authorRepository.findAll()).thenReturn(List.of(author));
    when(authorMapper.toAuthorDtoResponse(List.of(author))).thenReturn(List.of(authorDtoResponse));

    // Act
    List<AuthorDtoResponse> result = authorService.getAll();

    // Assert
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(authorDtoResponse);
    verify(authorListCache).put(cacheKey, List.of(authorDtoResponse));
    verify(authorRepository).findAll();
  }

  @Test
  void getById_ShouldReturnAuthorFromCache() {
    // Arrange
    Long authorId = 1L;
    when(authorCache.get(authorId)).thenReturn(authorDtoResponse);

    // Act
    AuthorDtoResponse result = authorService.getById(authorId);

    // Assert
    assertThat(result).isEqualTo(authorDtoResponse);
    verify(authorCache).get(authorId);
    verifyNoInteractions(authorRepository);
  }

  @Test
  void getById_ShouldFetchFromDBAndCacheWhenCacheEmpty() {
    // Arrange
    Long authorId = 1L;
    when(authorCache.get(authorId)).thenReturn(null);
    when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    // Act
    AuthorDtoResponse result = authorService.getById(authorId);

    // Assert
    assertThat(result).isEqualTo(authorDtoResponse);
    verify(authorCache).put(authorId, authorDtoResponse);
    verify(authorRepository).findById(authorId);
  }

  @Test
  void getById_ShouldThrowExceptionWhenAuthorNotFound() {
    // Arrange
    Long authorId = 99L;
    when(authorCache.get(authorId)).thenReturn(null);
    when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(RuntimeException.class, () -> authorService.getById(authorId));
    verify(authorRepository).findById(authorId);
  }

  @Test
  void create_ShouldSaveAuthorAndClearListCache() {
    // Arrange
    when(authorMapper.toAuthorEntity(authorDtoRequest)).thenReturn(author);
    when(authorRepository.save(author)).thenReturn(author);
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    // Act
    AuthorDtoResponse result = authorService.create(authorDtoRequest);

    // Assert
    assertThat(result).isEqualTo(authorDtoResponse);
    verify(authorCache).put(author.getId(), authorDtoResponse);
    verify(authorListCache).clear();
    verify(authorRepository).save(author);
  }

  @Test
  void update_ShouldUpdateAuthorAndClearCaches() {
    // Arrange
    Long authorId = 1L;
    when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
    when(authorMapper.toAuthorEntity(authorDtoRequest)).thenReturn(author);
    when(authorRepository.save(author)).thenReturn(author);
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    // Act
    AuthorDtoResponse result = authorService.update(authorId, authorDtoRequest);

    // Assert
    assertThat(result).isEqualTo(authorDtoResponse);
    verify(authorCache).put(authorId, authorDtoResponse);
    verify(authorListCache).clear();
    verify(authorRepository).save(author);
  }

  @Test
  void update_ShouldThrowExceptionWhenAuthorNotFound() {
    // Arrange
    Long authorId = 99L;
    when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(RuntimeException.class, () -> authorService.update(authorId, authorDtoRequest));
  }

  @Test
  void delete_ShouldRemoveAuthorAndClearCaches() {
    // Arrange
    Long authorId = 1L;
    when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
    doNothing().when(authorRepository).delete(author);

    // Act
    authorService.delete(authorId);

    // Assert
    verify(authorCache).remove(authorId);
    verify(authorListCache).clear();
    verify(authorRepository).delete(author);
  }

  @Test
  void getAuthorWithBooks_ShouldReturnAuthorWithBooks() {
    // Arrange
    Long authorId = 1L;
    when(authorRepository.findByIdWithBooks(authorId)).thenReturn(Optional.of(author));
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    // Act
    AuthorDtoResponse result = authorService.getAuthorWithBooks(authorId);

    // Assert
    assertThat(result).isEqualTo(authorDtoResponse);
    verify(authorRepository).findByIdWithBooks(authorId);
  }

  @Test
  void existsById_ShouldReturnTrueWhenAuthorExists() {
    // Arrange
    Long authorId = 1L;
    when(authorRepository.existsById(authorId)).thenReturn(true);

    // Act
    boolean result = authorService.existsById(authorId);

    // Assert
    assertThat(result).isTrue();
  }
}
