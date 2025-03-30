package com.example.weblibrary.service;

import com.example.weblibrary.WebLibraryApplication;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.Book;
import com.example.weblibrary.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = WebLibraryApplication.class)
@TestPropertySource(properties = "spring.config.name=application-test")
@AutoConfigureTestEntityManager
@Transactional
class AuthorRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private AuthorRepository authorRepository;

  private Author createValidAuthor(String name, String surname) {
    Author author = new Author();
    author.setName(name);
    author.setSurname(surname);
    // Set other required fields
    author.setPatronymic("");       // if required
    author.setRating(0.0);          // if required
    author.setGenreSpecialization("Fiction"); // if required
    return author;
  }
  @Test
  void shouldSaveAndRetrieveAuthor() {
    // Given
    Author author = createValidAuthor("John", "Doe");
    author = entityManager.persistAndFlush(author);

    // When
    Optional<Author> foundAuthor = authorRepository.findById(author.getId());

    // Then
    assertThat(foundAuthor)
        .isPresent()
        .hasValueSatisfying(a -> {
          assertThat(a.getName()).isEqualTo("John");
          assertThat(a.getSurname()).isEqualTo("Doe");
        });
  }

  @Test
  void shouldFindAuthorWithBooks() {
    // Given
    Author author = new Author();
    author.setName("J.K.");
    author.setSurname("Rowling");
    author.setPatronymic("");
    author.setRating(0.0);
    author.setGenreSpecialization("Fantasy");

    Book book1 = new Book();
    book1.setTitle("Harry Potter 1");
    book1.setAuthor(author);
    book1.setGenre("Fantasy"); // обязательное поле
    book1.setIsbn("1234567890"); // если обязательно
    book1.setPages(300); // если обязательно
    book1.setPublisher("Bloomsbury");
    book1.setLanguage("England");
    // если обязательно
     // если обязательно

    Book book2 = new Book();
    book2.setTitle("Harry Potter 2");
    book2.setAuthor(author);
    book2.setGenre("Fantasy"); // обязательное поле
    book2.setIsbn("1234567891"); // если обязательно
    book2.setPages(350); // если обязательно
    book2.setPublisher("Bloomsbury"); // если обязательно
    book2.setLanguage("England");
     // если обязательно

    author.setBooks(List.of(book1, book2));

    entityManager.persistAndFlush(author);
    entityManager.persistAndFlush(book1);
    entityManager.persistAndFlush(book2);

    // When
    Optional<Author> authorWithBooks = authorRepository.findByIdWithBooks(author.getId());

    // Then
    assertThat(authorWithBooks)
        .isPresent()
        .hasValueSatisfying(a -> {
          assertThat(a.getBooks())
              .hasSize(2)
              .extracting(Book::getTitle)
              .containsExactlyInAnyOrder("Harry Potter 1", "Harry Potter 2");
        });
  }

  @Test
  void shouldReturnEmptyWhenAuthorNotFound() {
    // When
    Optional<Author> result = authorRepository.findByIdWithBooks(999L);

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  void shouldDeleteAuthor() {
    // Given
    Author author = new Author();
    author.setName("Mark");
    author.setSurname("Twain");
    author.setPatronymic(""); // если поле обязательно
    author.setRating(0.0); // если поле обязательно
    author.setGenreSpecialization("Classic"); // если поле обязательно
    author = entityManager.persistAndFlush(author);

    // When
    authorRepository.deleteById(author.getId());
    entityManager.flush();

    // Then
    assertThat(authorRepository.findById(author.getId()))
        .isEmpty();
  }
}