package com.example.weblibrary.model;

import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.model.dto.BookDtoResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDtoResponseTest {

  @Test
  void testFullConstructor() {
    BookDtoResponse book = new BookDtoResponse(1L, "Test Book", "Test Publisher",
        "1234567890", 300L, "Fiction", LocalDate.of(2020, 1, 1), "English",
        "A great book", "example.com/image.jpg");
    AuthorDtoResponse response = new AuthorDtoResponse(
        1L, "John", "Doe", "Smith",
        LocalDate.of(1970, 1, 1),
        LocalDate.of(2020, 1, 1),
        "Biography", "Fantasy", 4.5,
        List.of(book)
    );

    assertEquals(1L, response.id());
    assertEquals("John", response.name());
    assertEquals("Doe", response.surname());
    assertEquals("Smith", response.patronymic());
    assertEquals(LocalDate.of(1970, 1, 1), response.birthDate());
    assertEquals(LocalDate.of(2020, 1, 1), response.deathDate());
    assertEquals("Biography", response.biography());
    assertEquals("Fantasy", response.genreSpecialization());
    assertEquals(4.5, response.rating());
    assertEquals(1, response.books().size());
    assertEquals("Test Book", response.books().get(0).title());
  }

  @Test
  void testSimplifiedConstructor() {
    AuthorDtoResponse response = new AuthorDtoResponse(1L, "John");

    assertEquals(1L, response.id());
    assertEquals("John", response.name());
    assertNull(response.surname());
    assertNull(response.patronymic());
    assertNull(response.birthDate());
    assertNull(response.deathDate());
    assertNull(response.biography());
    assertNull(response.genreSpecialization());
    assertNull(response.rating());
    assertNull(response.books());
  }
}