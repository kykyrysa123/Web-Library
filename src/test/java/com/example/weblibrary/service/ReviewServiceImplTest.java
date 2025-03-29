package com.example.weblibrary.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.weblibrary.WebLibraryApplication;
import com.example.weblibrary.mapper.ReviewMapper;
import com.example.weblibrary.mapper.ReviewMapperImpl;
import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.Review;
import com.example.weblibrary.model.User;
import com.example.weblibrary.model.dto.ReviewDtoRequest;
import com.example.weblibrary.model.dto.ReviewDtoResponse;
import com.example.weblibrary.repository.BookRepository;
import com.example.weblibrary.repository.ReviewRepository;
import com.example.weblibrary.repository.UserRepository;
import com.example.weblibrary.service.cache.SimpleCache;
import com.example.weblibrary.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = WebLibraryApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.name=application-test")
class ReviewServiceImplTest {

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private ReviewMapperImpl reviewMapper;

  @Mock
  private BookRepository bookRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private ReviewServiceImpl reviewService;

  private Review review;
  private ReviewDtoRequest reviewDtoRequest;
  private ReviewDtoResponse reviewDtoResponse;
  private Book book;
  private User user;

  @BeforeEach
  void setUp() {
    // Создание объекта Book с корректными значениями
    book = new Book(1L, "Test Book", "Author Name", "123-4567891234", 300, "Fiction",LocalDate.of(1980, 1, 1),"English", "asd", "http://image.url",4.0);

    // Создание объекта User с корректными значениями
    user = new User(1L, "testuser", "Test", "User",30,"asd","man","belarus","testuser@example.com", "hashedPassword", LocalDate.of(2020, 5, 1), LocalDate.now());

    // Создание объекта Review с корректными значениями
    review = new Review(1L, book, user, 4.5, "Great book!", LocalDate.now());

    reviewDtoRequest = new ReviewDtoRequest(null, 1L, 1L, 4.5, "Great book!", LocalDate.now());
    reviewDtoResponse = new ReviewDtoResponse(1L, null, null, 4.5, "Great book!", LocalDate.now());
  }

  @Test
  void testGetAll() {
    when(reviewRepository.findAll()).thenReturn(List.of(review));
    when(reviewMapper.toReviewDtoResponse(List.of(review))).thenReturn(List.of(reviewDtoResponse));

    List<ReviewDtoResponse> result = reviewService.getAll();

    assertEquals(1, result.size());
    assertEquals("Great book!", result.get(0).reviewText());
  }

  @Test
  void testGetById() {
    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
    when(reviewMapper.toReviewDtoResponse(review)).thenReturn(reviewDtoResponse);

    ReviewDtoResponse result = reviewService.getById(1L);

    assertNotNull(result);
    assertEquals("Great book!", result.reviewText());
  }

  @Test
  void testGetById_NotFound() {
    when(reviewRepository.findById(2L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> reviewService.getById(2L));

    assertTrue(exception.getMessage().contains("Review not found with id: 2"));
  }

  @Test
  void testCreate() {
    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(reviewMapper.toReviewEntity(reviewDtoRequest)).thenReturn(review);
    when(reviewRepository.save(review)).thenReturn(review);
    when(reviewMapper.toReviewDtoResponse(review)).thenReturn(reviewDtoResponse);

    ReviewDtoResponse result = reviewService.create(reviewDtoRequest);

    assertNotNull(result);
    assertEquals("Great book!", result.reviewText());
  }

  @Test
  void testCreate_BookNotFound() {
    when(bookRepository.findById(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> reviewService.create(reviewDtoRequest));

    assertTrue(exception.getMessage().contains("Book not found with id: 1"));
  }

  @Test
  void testCreate_UserNotFound() {
    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> reviewService.create(reviewDtoRequest));

    assertTrue(exception.getMessage().contains("User not found with id: 1"));
  }

  @Test
  void testUpdate() {
    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
    when(reviewMapper.toReviewEntity(reviewDtoRequest)).thenReturn(review);
    when(reviewRepository.save(review)).thenReturn(review);
    when(reviewMapper.toReviewDtoResponse(review)).thenReturn(reviewDtoResponse);

    ReviewDtoResponse result = reviewService.update(1L, reviewDtoRequest);

    assertNotNull(result);
    assertEquals("Great book!", result.reviewText());
  }

  @Test
  void testUpdate_NotFound() {
    when(reviewRepository.findById(2L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> reviewService.update(2L, reviewDtoRequest));

    assertTrue(exception.getMessage().contains("Review not found with id: 2"));
  }

  @Test
  void testDelete() {
    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
    doNothing().when(reviewRepository).delete(review);

    assertDoesNotThrow(() -> reviewService.delete(1L));
  }

  @Test
  void testDelete_NotFound() {
    when(reviewRepository.findById(2L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> reviewService.delete(2L));

    assertTrue(exception.getMessage().contains("Review not found with id: 2"));
  }
}
