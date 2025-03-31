package com.example.weblibrary.model;

import com.example.weblibrary.model.dto.ReviewDtoRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReviewDtoRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testValidReviewDtoRequest() {
    ReviewDtoRequest request = new ReviewDtoRequest(
        null, 1L, 1L, 4.5, "Great book!", LocalDate.now()
    );

    Set<ConstraintViolation<ReviewDtoRequest>> violations = validator.validate(request);
    assertTrue(violations.isEmpty());
    assertNull(request.id());
    assertEquals(1L, request.bookId());
    assertEquals(1L, request.userId());
    assertEquals(4.5, request.rating());
    assertEquals("Great book!", request.reviewText());
    assertEquals(LocalDate.now(), request.reviewDate());
  }

  @Test
  void testSimplifiedConstructor() {
    ReviewDtoRequest request = new ReviewDtoRequest("Great book!");

    Set<ConstraintViolation<ReviewDtoRequest>> violations = validator.validate(request);
    assertEquals(3, violations.size()); // bookId, userId, rating - null
    assertNull(request.id());
    assertNull(request.bookId());
    assertNull(request.userId());
    assertNull(request.rating());
    assertEquals("Great book!", request.reviewText());
    assertNull(request.reviewDate());
  }

  @Test
  void testIdNotNull() {
    ReviewDtoRequest request = new ReviewDtoRequest(1L, 1L, 1L, 4.5, "Text", LocalDate.now());

    Set<ConstraintViolation<ReviewDtoRequest>> violations = validator.validate(request);
    assertEquals(1, violations.size());
    assertEquals("ID should not be provided for new reviews", violations.iterator().next().getMessage());
  }

  @Test
  void testBookIdNull() {
    ReviewDtoRequest request = new ReviewDtoRequest(null, null, 1L, 4.5, "Text", LocalDate.now());

    Set<ConstraintViolation<ReviewDtoRequest>> violations = validator.validate(request);
    assertEquals(1, violations.size());
    assertEquals("Book ID is required", violations.iterator().next().getMessage());
  }

  @Test
  void testRatingOutOfBounds() {
    ReviewDtoRequest request = new ReviewDtoRequest(null, 1L, 1L, 5.5, "Text", LocalDate.now());

    Set<ConstraintViolation<ReviewDtoRequest>> violations = validator.validate(request);
    assertEquals(1, violations.size());
    assertEquals("Rating must be at most 5.0", violations.iterator().next().getMessage());
  }
}