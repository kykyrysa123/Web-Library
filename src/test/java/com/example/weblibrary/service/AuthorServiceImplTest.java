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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

  @Mock private AuthorRepository authorRepository;
  @Mock private AuthorMapperImpl authorMapper;
  @Mock private SimpleCache<Long, AuthorDtoResponse> authorCache;
  @Mock private SimpleCache<String, List<AuthorDtoResponse>> authorListCache;

  @InjectMocks private AuthorServiceImpl authorService;

  private Author author;
  private AuthorDtoRequest authorDtoRequest;
  private AuthorDtoResponse authorDtoResponse;

  @BeforeEach
  void setUp() {
    author = new Author();
    author.setId(1L);
    author.setName("J.R.R. Tolkien");

    authorDtoRequest = new AuthorDtoRequest("J.R.R. Tolkien");
    authorDtoResponse = new AuthorDtoResponse(1L, "J.R.R. Tolkien");

    lenient().when(authorRepository.findById(99L)).thenReturn(Optional.empty());
    lenient().doNothing().when(authorCache).put(anyLong(), any());
    lenient().when(authorCache.remove(anyLong())).thenReturn(null);
    lenient().doNothing().when(authorListCache).put(anyString(), any());
  }

  @Test
  void getAll_ShouldReturnAuthorsFromCache() {
    String cacheKey = "all_authors";
    when(authorListCache.get(cacheKey)).thenReturn(List.of(authorDtoResponse));

    List<AuthorDtoResponse> result = authorService.getAll();

    assertThat(result).hasSize(1).containsExactly(authorDtoResponse);
    verify(authorListCache).get(cacheKey);
    verifyNoInteractions(authorRepository);
  }

  @Test
  void getAll_ShouldFetchFromDBAndCacheWhenCacheEmpty() {
    String cacheKey = "all_authors";
    when(authorListCache.get(cacheKey)).thenReturn(null);
    when(authorRepository.findAll()).thenReturn(List.of(author));
    when(authorMapper.toAuthorDtoResponse(List.of(author))).thenReturn(List.of(authorDtoResponse));

    List<AuthorDtoResponse> result = authorService.getAll();

    assertThat(result).hasSize(1).containsExactly(authorDtoResponse);
    verify(authorListCache).put(eq(cacheKey), any());
    verify(authorRepository).findAll();
  }

  @Test
  void getById_ShouldReturnAuthorFromCache() {
    Long authorId = 1L;
    when(authorCache.get(authorId)).thenReturn(authorDtoResponse);

    AuthorDtoResponse result = authorService.getById(authorId);

    assertThat(result).isEqualTo(authorDtoResponse);
    verify(authorCache).get(authorId);
    verifyNoInteractions(authorRepository);
  }

  @Test
  void getById_ShouldFetchFromDBAndCacheWhenCacheEmpty() {
    Long authorId = 1L;
    when(authorCache.get(authorId)).thenReturn(null);
    when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    AuthorDtoResponse result = authorService.getById(authorId);

    assertThat(result).isEqualTo(authorDtoResponse);
    verify(authorCache).put(authorId, authorDtoResponse);
    verify(authorRepository).findById(authorId);
  }

  @Test
  void getById_ShouldThrowExceptionWhenAuthorNotFound() {
    Long authorId = 99L;

    assertThrows(RuntimeException.class, () -> authorService.getById(authorId));
    verify(authorRepository).findById(authorId);
  }

  @Test
  void create_ShouldSaveAuthorAndClearListCache() {
    when(authorMapper.toAuthorEntity(authorDtoRequest)).thenReturn(author);
    when(authorRepository.save(author)).thenReturn(author);
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    AuthorDtoResponse result = authorService.create(authorDtoRequest);

    assertThat(result).isEqualTo(authorDtoResponse);
    verify(authorCache).put(author.getId(), authorDtoResponse);
    verify(authorListCache).clear();
    verify(authorRepository).save(author);
  }

  @Test
  void update_ShouldUpdateAuthorAndClearCaches() {
    Long authorId = 1L;
    when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
    when(authorMapper.toAuthorEntity(authorDtoRequest)).thenReturn(author);
    when(authorRepository.save(author)).thenReturn(author);
    when(authorMapper.toAuthorDtoResponse(author)).thenReturn(authorDtoResponse);

    AuthorDtoResponse result = authorService.update(authorId, authorDtoRequest);

    assertThat(result).isEqualTo(authorDtoResponse);
    verify(authorCache).put(authorId, authorDtoResponse);
    verify(authorListCache).clear();
    verify(authorRepository).save(author);
  }

  @Test
  void delete_ShouldRemoveAuthorAndClearCaches() {
    Long authorId = 1L;
    when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
    doNothing().when(authorRepository).delete(author);

    authorService.delete(authorId);

    verify(authorRepository).delete(any(Author.class));
    verify(authorListCache).clear();
    verify(authorRepository).delete(author);
  }

  @Test
  void existsById_ShouldReturnTrueWhenAuthorExists() {
    Long authorId = 1L;
    when(authorRepository.existsById(authorId)).thenReturn(true);

    boolean result = authorService.existsById(authorId);

    assertThat(result).isTrue();
    verify(authorRepository).existsById(authorId);
  }
}