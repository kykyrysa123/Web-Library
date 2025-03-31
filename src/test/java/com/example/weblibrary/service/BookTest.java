package com.example.weblibrary.service;

import com.example.weblibrary.WebLibraryApplication;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.Review;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = WebLibraryApplication.class)
@TestPropertySource(properties = "spring.config.name=application-test")
@AutoConfigureTestEntityManager
@Transactional
class BookTest {

  @Autowired
  private TestEntityManager entityManager;

  @Test
  void testBookCreationWithRequiredFields() {
    // Given
    Author author = createValidAuthor("Лев", "Толстой");
    Book book = new Book();
    book.setTitle("Война и мир");
    book.setPublisher("Русский вестник");
    book.setGenre("Роман");
    book.setLanguage("Русский");
    book.setAuthor(author);

    // When
    Book savedBook = entityManager.persist(book);
    entityManager.flush();

    // Then
    assertThat(savedBook.getId()).isNotNull();
    assertThat(savedBook.getTitle()).isEqualTo("Война и мир");
    assertThat(savedBook.getAuthor().getName()).isEqualTo("Лев");
  }

  @Test
  void testBookCreationWithAllFields() {
    // Given
    Author author = createValidAuthor("Фёдор", "Достоевский");
    Book book = new Book();
    book.setTitle("Преступление и наказание");
    book.setPublisher("Русский вестник");
    book.setIsbn("978-5-699-12014-7");
    book.setPages(608);
    book.setGenre("Роман");
    book.setPublishDate(LocalDate.of(1866, 1, 1));
    book.setLanguage("Русский");
    book.setDescription("Роман о нравственных последствиях преступления");
    book.setImageUrl("http://example.com/crime.jpg");
    book.setRating(4.8);
    book.setAuthor(author);

    // When
    Book savedBook = entityManager.persist(book);
    entityManager.flush();

    // Then
    assertThat(savedBook.getId()).isNotNull();
    assertThat(savedBook.getIsbn()).isEqualTo("978-5-699-12014-7");
    assertThat(savedBook.getPages()).isEqualTo(608);
    assertThat(savedBook.getRating()).isEqualTo(4.8);
  }



  @Test
  void testBookWithoutOptionalFields() {
    // Given
    Author author = createValidAuthor("Александр", "Пушкин");
    Book book = new Book();
    book.setTitle("Евгений Онегин");
    book.setPublisher("Современник");
    book.setGenre("Роман в стихах");
    book.setLanguage("Русский");
    book.setAuthor(author);

    // When
    Book savedBook = entityManager.persist(book);
    entityManager.flush();

    // Then
    assertThat(savedBook.getIsbn()).isNull();
    assertThat(savedBook.getPages()).isNull();
    assertThat(savedBook.getRating()).isNull();
  }

  private Author createValidAuthor(String name, String surname) {
    Author author = new Author();
    author.setName(name);
    author.setSurname(surname);
    author.setPatronymic("");
    author.setRating(0.0);
    author.setGenreSpecialization("Классика");
    return entityManager.persist(author);
  }

  private Book createValidBook(String title, String genre, Author author) {
    Book book = new Book();
    book.setTitle(title);
    book.setGenre(genre);
    book.setAuthor(author);
    book.setPublisher("Издательство");
    book.setLanguage("Русский");
    book.setIsbn("ISBN-" + System.currentTimeMillis());
    book.setPages(300);
    book.setPublishDate(LocalDate.now().minusYears(1));
    book.setDescription("Описание книги");
    book.setImageUrl("example.com/image.jpg");
    book.setRating(4.5);
    return entityManager.persist(book);
  }

  /*private Review createValidReview(String text, int rating, Book book) {
    Review review = new Review();
    review.setText(text);
    review.setRating(rating);
    review.setBook(book);
    review.setReviewerName("Читатель");
    review.setReviewDate(LocalDate.now());
    return review;
  }*/
}