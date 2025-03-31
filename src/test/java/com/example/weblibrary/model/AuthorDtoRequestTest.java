package com.example.weblibrary.model;

import com.example.weblibrary.model.dto.AuthorDtoRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDtoRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testValidAuthorDtoRequest() {
    AuthorDtoRequest request = new AuthorDtoRequest(
        "John", "Doe", "Smith",
        LocalDate.of(1970, 1, 1),
        LocalDate.of(2020, 1, 1),
        "Biography", "Fantasy", 4.5
    );

    Set<ConstraintViolation<AuthorDtoRequest>> violations = validator.validate(request);
    assertTrue(violations.isEmpty());
    assertEquals("John", request.name());
    assertEquals("Doe", request.surname());
    assertEquals("Smith", request.patronymic());
    assertEquals(LocalDate.of(1970, 1, 1), request.birthDate());
    assertEquals(LocalDate.of(2020, 1, 1), request.deathDate());
    assertEquals("Biography", request.biography());
    assertEquals("Fantasy", request.genreSpecialization());
    assertEquals(4.5, request.rating());
  }

  @Test
  void testSimplifiedConstructor() {
    AuthorDtoRequest request = new AuthorDtoRequest("John");

    Set<ConstraintViolation<AuthorDtoRequest>> violations = validator.validate(request);
    assertTrue(violations.isEmpty());
    assertEquals("John", request.name());
    assertNull(request.surname());
    assertNull(request.patronymic());
    assertNull(request.birthDate());
    assertNull(request.deathDate());
    assertNull(request.biography());
    assertNull(request.genreSpecialization());
    assertNull(request.rating());
  }

  @Test
  void testNameTooLong() {
    String longName = "a".repeat(101);
    AuthorDtoRequest request = new AuthorDtoRequest(longName);

    Set<ConstraintViolation<AuthorDtoRequest>> violations = validator.validate(request);
    assertEquals(1, violations.size());
    assertEquals("Name must not exceed 100 characters", violations.iterator().next().getMessage());
  }

  @Test
  void testBirthDateInFuture() {
    AuthorDtoRequest request = new AuthorDtoRequest(
        "John", null, null,
        LocalDate.now().plusDays(1), null, null, null, null
    );

    Set<ConstraintViolation<AuthorDtoRequest>> violations = validator.validate(request);
    assertEquals(1, violations.size());
    assertEquals("Birth date must be in the past", violations.iterator().next().getMessage());
  }

  @Test
  void testRatingOutOfBounds() {
    AuthorDtoRequest request = new AuthorDtoRequest(
        "John", null, null, null, null, null, null, 5.1
    );

    Set<ConstraintViolation<AuthorDtoRequest>> violations = validator.validate(request);
    assertEquals(1, violations.size());
    assertEquals("Rating must be at most 5.0", violations.iterator().next().getMessage());
  }
}