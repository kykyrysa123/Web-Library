/*
package com.example.weblibrary.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.weblibrary.WebLibraryApplication;
import com.example.weblibrary.mapper.BookMapperImpl;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.repository.BookRepository;
import com.example.weblibrary.service.cache.SimpleCache;
import com.example.weblibrary.service.impl.BookServiceImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("FieldCanBeLocal")
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = WebLibraryApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.name=application-test")
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

  @Mock
  private BookRepository bookRepository;

  @Mock
  private BookMapperImpl bookMapper;

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
    author = new Author(1L, "John", "Doe", "asd", LocalDate.of(2020, 1, 1), LocalDate.of(2025, 1, 1), "goodLife", "fantasy", 4.0);
    book = new Book(1L, "Test Book", "Test Publisher", "1234567890", 300, "Fiction", LocalDate.of(2020, 1, 1), "English", "A great book", "example.com/image.jpg", 4.5);
    book.setAuthor(author);
    bookDtoRequest = new BookDtoRequest("Test Book", "Test Publisher", "1234567890", 300, "Fiction", LocalDate.of(2020, 1, 1), "English", "A great book", "example.com/image.jpg", 4.5, 1L);
    bookDtoResponse = new BookDtoResponse(1L, "Test Book", "Test Publisher", "1234567890", 300L, "Fiction", LocalDate.of(2020, 1, 1), "English", "A great book", "example.com/image.jpg");
  }

  @Test
  void testGetAll() {
    lenient().when(bookCache.get(1L)).thenReturn(null);
    when(bookRepository.findAll()).thenReturn(List.of(book));
    when(bookMapper.toBookDtoResponse(List.of(book))).thenReturn(List.of(bookDtoResponse));

    List<BookDtoResponse> result = bookService.getAll();

    assertEquals(1, result.size());
    assertEquals("MyTitle", result.get(0).title());
  }

  @Test
  void testGetById() {
    lenient().when(bookCache.get(1L)).thenReturn(null);
    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
    when(bookMapper.toBookDtoResponse(book)).thenReturn(bookDtoResponse);

    BookDtoResponse result = bookService.getById(1L);

    assertNotNull(result);
    assertEquals("John", result.title());
  }

  @Test
  void testGetById_NotFound() {
    when(bookRepository.findById(2L)).thenReturn(Optional.empty());
    Exception exception = assertThrows(RuntimeException.class, () -> bookService.getById(2L));
    assertTrue(exception.getMessage().contains("Book not found"));
  }

  @Test
  void testCreate() {
    when(bookMapper.toBookEntity(bookDtoRequest)).thenReturn(book);
    when(bookRepository.save(book)).thenReturn(book);
    when(bookMapper.toBookDtoResponse(book)).thenReturn(bookDtoResponse);

    BookDtoResponse result = bookService.create(bookDtoRequest);

    assertNotNull(result);
    assertEquals("Test Book", result.title());
  }

  @Test
  void testDelete() {
    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
    doNothing().when(bookRepository).delete(book);

    assertDoesNotThrow(() -> bookService.delete(1L));
  }

  @Test
  void testDelete_NotFound() {
    when(bookRepository.findById(2L)).thenReturn(Optional.empty());
    Exception exception = assertThrows(RuntimeException.class, () -> bookService.delete(2L));
    assertTrue(exception.getMessage().contains("book not found"));
  }
}
*/
