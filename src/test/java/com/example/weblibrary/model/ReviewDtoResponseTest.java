package com.example.weblibrary.model;

import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.model.dto.ReviewDtoResponse;
import com.example.weblibrary.model.dto.UserDtoResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReviewDtoResponseTest {

  @Test
  void testFullConstructor() {
    BookDtoResponse book = new BookDtoResponse(1L, "Test Book", "Test Publisher",
        "1234567890", 300L, "Fiction", LocalDate.of(2020, 1, 1), "English",
        "A great book", "example.com/image.jpg");
    UserDtoResponse user = new UserDtoResponse(1L, "user@example.com");
    ReviewDtoResponse response = new ReviewDtoResponse(
        1L, book, user, 4.5, "Great book!", LocalDate.now()
    );

    assertEquals(1L, response.id());
    assertEquals("Test Book", response.book().title());
    assertEquals(null, response.user().email());
    assertEquals(4.5, response.rating());
    assertEquals("Great book!", response.reviewText());
    assertEquals(LocalDate.now(), response.reviewDate());
  }

  @Test
  void testSimplifiedConstructor() {
    ReviewDtoResponse response = new ReviewDtoResponse(1L, "Great book!");

    assertEquals(1L, response.id());
    assertNull(response.book());
    assertNull(response.user());
    assertNull(response.rating());
    assertEquals("Great book!", response.reviewText());
    assertNull(response.reviewDate());
  }
}