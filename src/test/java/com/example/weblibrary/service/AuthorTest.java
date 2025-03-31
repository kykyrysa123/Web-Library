package com.example.weblibrary.service;

import com.example.weblibrary.WebLibraryApplication;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.Book;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = WebLibraryApplication.class)
@TestPropertySource(properties = "spring.config.name=application-test")
@AutoConfigureTestEntityManager
@Transactional
class AuthorTest {

  @Autowired
  private TestEntityManager entityManager;

  @Test
  void testAuthorCreation() {
    // Given
    Author author = new Author();
    author.setName("Фёдор");
    author.setSurname("Достоевский");
    author.setPatronymic("Михайлович");
    author.setBirthDate(LocalDate.of(1821, 11, 11));
    author.setDeathDate(LocalDate.of(1881, 2, 9));
    author.setBiography("Великий русский писатель");
    author.setGenreSpecialization("Роман");
    author.setRating(5.0);

    // When
    Author savedAuthor = entityManager.persist(author);
    entityManager.flush();

    // Then
    assertThat(savedAuthor.getId()).isNotNull();
    assertThat(savedAuthor.getName()).isEqualTo("Фёдор");
    assertThat(savedAuthor.getSurname()).isEqualTo("Достоевский");
    assertThat(savedAuthor.getFullName()).isEqualTo("Достоевский Фёдор Михайлович");
  }

  @Test
  void testAuthorWithBooksRelationship() {
    // Given
    Author author = createValidAuthor("Лев", "Толстой");
    Book book1 = createValidBook("Война и мир", "Роман", author);
    Book book2 = createValidBook("Анна Каренина", "Роман", author);

    // When
    Author persistedAuthor = entityManager.persist(author);
    entityManager.persist(book1);
    entityManager.persist(book2);
    entityManager.flush();
    entityManager.clear();

    Author retrievedAuthor = entityManager.find(Author.class, persistedAuthor.getId());

    // Then
    assertThat(retrievedAuthor.getBooks())
        .hasSize(2)
        .extracting(Book::getTitle)
        .containsExactlyInAnyOrder("Война и мир", "Анна Каренина");
  }

  @Test
  void testAuthorWithoutPatronymic() {
    // Given
    Author author = new Author();
    author.setName("Агата");
    author.setSurname("Кристи");
    author.setBirthDate(LocalDate.of(1890, 9, 15));
    author.setGenreSpecialization("Детектив");

    // When
    Author savedAuthor = entityManager.persist(author);
    entityManager.flush();

    // Then
    assertThat(savedAuthor.getPatronymic()).isNull();
    assertThat(savedAuthor.getFullName()).isEqualTo("Кристи Агата");
  }

  @Test
  void testAuthorRating() {
    // Given
    Author author = createValidAuthor("Антон", "Чехов");
    author.setRating(4.8);

    // When
    Author savedAuthor = entityManager.persist(author);
    entityManager.flush();

    // Then
    assertThat(savedAuthor.getRating()).isEqualTo(4.8);
  }

  @Test
  void testLivingAuthor() {
    // Given
    Author author = createValidAuthor("Стивен", "Кинг");
    author.setBirthDate(LocalDate.of(1947, 9, 21));
    author.setDeathDate(null);

    // When
    Author savedAuthor = entityManager.persist(author);
    entityManager.flush();

    // Then
    assertThat(savedAuthor.getDeathDate()).isNull();
    assertThat(savedAuthor.isDeceased()).isFalse();
  }

  @Test
  void testDeceasedAuthor() {
    // Given
    Author author = createValidAuthor("Эрнест", "Хемингуэй");
    author.setBirthDate(LocalDate.of(1899, 7, 21));
    author.setDeathDate(LocalDate.of(1961, 7, 2));

    // When
    Author savedAuthor = entityManager.persist(author);
    entityManager.flush();

    // Then
    assertThat(savedAuthor.getDeathDate()).isNotNull();
    assertThat(savedAuthor.isDeceased()).isTrue();
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
    book.setIsbn("ISBN-" + System.currentTimeMillis());
    book.setPages(300);
    book.setPublisher("Издательство");
    book.setPublishDate(LocalDate.now().minusYears(1));
    book.setLanguage("Русский");
    book.setDescription("Описание книги");
    book.setImageUrl("example.com/image.jpg");
    book.setRating(4.5);
    return entityManager.persist(book);
  }
}