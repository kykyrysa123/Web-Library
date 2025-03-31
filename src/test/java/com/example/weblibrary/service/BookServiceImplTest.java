package com.example.weblibrary.service;

import com.example.weblibrary.mapper.BookMapperImpl;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.repository.AuthorRepository;
import com.example.weblibrary.repository.BookRepository;
import com.example.weblibrary.service.cache.SimpleCache;
import com.example.weblibrary.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class BookServiceImplTest {

  @Mock
  private BookRepository bookRepository;

  @Mock
  private BookMapperImpl bookMapper;

  @Mock
  private AuthorRepository authorRepository;

  @Mock
  private SimpleCache<Long, BookDtoResponse> bookCache;

  @Mock
  private SimpleCache<String, List<BookDtoResponse>> bookListCache;

  @InjectMocks
  private BookServiceImpl bookService;

  private Book book;
  private BookDtoRequest bookDtoRequest;
  private BookDtoResponse bookDtoResponse;
  private Author author;

  @BeforeEach
  void setUp() {
    author = new Author(1L, "John", "Doe", "asd", LocalDate.of(1970, 1, 1),
        LocalDate.of(2020, 1, 1), "goodLife", "fantasy", 4.0);
    book = new Book(1L, "Test Book", "Test Publisher", "1234567890", 300,
        "Fiction", LocalDate.of(2020, 1, 1), "English", "A great book",
        "example.com/image.jpg", 4.5);
    book.setAuthor(author);
    bookDtoRequest = new BookDtoRequest("Test Book", "Test Publisher",
        "1234567890", 300, "Fiction", LocalDate.of(2020, 1, 1), "English",
        "A great book", "example.com/image.jpg", 4.5, 1L);
    bookDtoResponse = new BookDtoResponse(1L, "Test Book", "Test Publisher",
        "1234567890", 300L, "Fiction", LocalDate.of(2020, 1, 1), "English",
        "A great book", "example.com/image.jpg");
  }

  @Test
  void testGetAll_FromCache() {
    // Явная инициализация сервиса с моками
    BookServiceImpl bookService = new BookServiceImpl(bookRepository, bookMapper, authorRepository, bookCache, bookListCache);

    when(bookListCache.get("all_books")).thenReturn(List.of(bookDtoResponse));

    List<BookDtoResponse> result = bookService.getAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Book", result.get(0).title());
    verify(bookRepository, never()).findAll();
  }

  @Test
  void testGetAll_FromDatabase() {
    when(bookListCache.get("all_books")).thenReturn(null);
    when(bookRepository.findAll()).thenReturn(List.of(book));
    when(bookMapper.toBookDtoResponse(anyList())).thenReturn(List.of(bookDtoResponse));

    List<BookDtoResponse> result = bookService.getAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Book", result.get(0).title());
    //verify(bookListCache).put("all_books", List.of(bookDtoResponse));
  }

  @Test
  void testGetById_FromCache() {
    // Явная инициализация сервиса с моками
    BookServiceImpl bookService = new BookServiceImpl(bookRepository, bookMapper, authorRepository, bookCache, bookListCache);

    when(bookCache.get(1L)).thenReturn(bookDtoResponse);

    BookDtoResponse result = bookService.getById(1L);

    assertNotNull(result);
    assertEquals("Test Book", result.title());
    verify(bookRepository, never()).findById(anyLong());
  }

  @Test
  void testGetById_FromDatabase() {
    // Явная инициализация сервиса с моками
    BookServiceImpl bookService = new BookServiceImpl(bookRepository, bookMapper, authorRepository, bookCache, bookListCache);

    when(bookCache.get(1L)).thenReturn(null);
    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
    when(bookMapper.toBookDtoResponse(book)).thenReturn(bookDtoResponse);

    BookDtoResponse result = bookService.getById(1L);

    assertNotNull(result);
    assertEquals("Test Book", result.title());
    verify(bookCache).put(eq(1L), any(BookDtoResponse.class));
  }

  @Test
  void testGetById_NotFound() {
    // Явная инициализация сервиса с моками для консистентности
    BookServiceImpl bookService = new BookServiceImpl(bookRepository, bookMapper, authorRepository, bookCache, bookListCache);

    when(bookRepository.findById(2L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> bookService.getById(2L));
    assertEquals("Книга не найдена с ID: 2", exception.getMessage());
  }

  @Test
  void testCreate_Success() {
    BookServiceImpl bookService = new BookServiceImpl(bookRepository, bookMapper, authorRepository, bookCache, bookListCache);
    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
    when(bookMapper.toBookEntity(bookDtoRequest)).thenReturn(book);
    when(bookRepository.save(book)).thenReturn(book);
    when(bookMapper.toBookDtoResponse(book)).thenReturn(bookDtoResponse);

    BookDtoResponse result = bookService.create(bookDtoRequest);

    assertNotNull(result);
    assertEquals("Test Book", result.title());
    verify(bookCache).put(eq(1L), any(BookDtoResponse.class));
    verify(bookListCache).clear();
  }

  @Test
  void testCreate_AuthorNotFound() {
    when(authorRepository.findById(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> bookService.create(bookDtoRequest));
    assertEquals("Автор не найдена с ID: 1", exception.getMessage());
    verify(bookRepository, never()).save(any());
  }

  @Test
  void testUpdate_Success() {
    BookServiceImpl bookService = new BookServiceImpl(bookRepository, bookMapper, authorRepository, bookCache, bookListCache);

    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
    when(bookRepository.save(book)).thenReturn(book);
    when(bookMapper.toBookDtoResponse(book)).thenReturn(bookDtoResponse);

    BookDtoResponse result = bookService.update(1L, bookDtoRequest);

    assertNotNull(result);
    assertEquals("Test Book", result.title());
    verify(bookCache).put(eq(1L), any(BookDtoResponse.class));
    verify(bookListCache).clear();
  }

  @Test
  void testUpdate_BookNotFound() {
    when(bookRepository.findById(2L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> bookService.update(2L, bookDtoRequest));
    assertEquals("Книга не найдена с ID: 2", exception.getMessage());
    verify(authorRepository, never()).findById(anyLong());
  }

  @Test
  void testUpdate_AuthorNotFound() {
    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
    when(authorRepository.findById(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> bookService.update(1L, bookDtoRequest));
    assertEquals("Автор не найдена с ID: 1", exception.getMessage());
    verify(bookRepository, never()).save(any());
  }

  @Test
  void testDelete_Success() {
    // Явная инициализация сервиса с моками
    BookServiceImpl bookService = new BookServiceImpl(bookRepository, bookMapper, authorRepository, bookCache, bookListCache);

    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
    doNothing().when(bookRepository).delete(book);

    bookService.delete(1L);

    verify(bookCache).remove(1L);
    verify(bookListCache).clear();
  }

  @Test
  void testDelete_NotFound() {
    when(bookRepository.findById(2L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> bookService.delete(2L));
    assertEquals("Книга не найдена с ID: 2", exception.getMessage());
    verify(bookRepository, never()).delete(any());
  }

  @Test
  void testGetByGenre_Found() {
    when(bookRepository.findByGenre("Fiction")).thenReturn(List.of(book));
    when(bookMapper.toBookDtoResponse(anyList())).thenReturn(List.of(bookDtoResponse));

    List<BookDtoResponse> result = bookService.getByGenre("Fiction");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Book", result.get(0).title());
  }

  @Test
  void testGetByGenre_NotFound() {
    when(bookRepository.findByGenre("NonFiction")).thenReturn(Collections.emptyList());

    List<BookDtoResponse> result = bookService.getByGenre("NonFiction");

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testGetBooksByAuthorName_Found() {
    when(bookRepository.findByAuthorName("John Doe")).thenReturn(List.of(book));
    when(bookMapper.toBookDtoResponse(anyList())).thenReturn(List.of(bookDtoResponse));

    List<BookDtoResponse> result = bookService.getBooksByAuthorName("John Doe");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Book", result.get(0).title());
  }

  @Test
  void testGetBooksByAuthorName_NotFound() {
    when(bookRepository.findByAuthorName("Unknown")).thenReturn(Collections.emptyList());

    List<BookDtoResponse> result = bookService.getBooksByAuthorName("Unknown");

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testGetBookByTitle_Found() {
    when(bookRepository.findByTitleLike("Test")).thenReturn(List.of(book));
    when(bookMapper.toBookDtoResponse(anyList())).thenReturn(List.of(bookDtoResponse));

    List<BookDtoResponse> result = bookService.getBookByTitle("Test");

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Book", result.get(0).title());
  }

  @Test
  void testGetBookByTitle_NotFound() {
    when(bookRepository.findByTitleLike("Unknown")).thenReturn(Collections.emptyList());

    List<BookDtoResponse> result = bookService.getBookByTitle("Unknown");

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testCreateBooksBulk_Success() {
    // Явная инициализация сервиса с моками
    BookServiceImpl bookService = new BookServiceImpl(bookRepository, bookMapper, authorRepository, bookCache, bookListCache);

    List<BookDtoRequest> requests = List.of(bookDtoRequest);
    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
    when(bookMapper.toBookEntity(bookDtoRequest)).thenReturn(book);
    when(bookRepository.saveAll(anyList())).thenReturn(List.of(book));
    when(bookMapper.toBookDtoResponse(book)).thenReturn(bookDtoResponse);
    when(bookMapper.toBookDtoResponse(anyList())).thenReturn(List.of(bookDtoResponse));

    List<BookDtoResponse> result = bookService.createBooksBulk(requests);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Book", result.get(0).title());
    verify(bookCache).put(eq(1L), any(BookDtoResponse.class));
    verify(bookListCache).clear();
  }

  @Test
  void testCreateBooksBulk_AuthorNotFound() {
    List<BookDtoRequest> requests = List.of(bookDtoRequest);
    when(authorRepository.findById(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> bookService.createBooksBulk(requests));
    assertEquals("Автор не найдена с ID: 1", exception.getMessage());
    verify(bookRepository, never()).saveAll(any());
  }
}