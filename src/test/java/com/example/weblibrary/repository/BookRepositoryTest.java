package com.example.weblibrary.repository;

import com.example.weblibrary.WebLibraryApplication;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.Book;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = WebLibraryApplication.class)
@TestPropertySource(properties = "spring.config.name=application-test")
@AutoConfigureTestEntityManager
@Transactional
class BookRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private BookRepository bookRepository;

  @Test
  void findByAuthorName_shouldReturnBooksByAuthor() {
    // Given
    Author author = createValidAuthor("Лев", "Толстой");
    Book book1 = createValidBook("Война и мир", "Роман", author);
    Book book2 = createValidBook("Анна Каренина", "Роман", author);
    Author otherAuthor = createValidAuthor("Фёдор", "Достоевский");
    createValidBook("Преступление и наказание", "Роман", otherAuthor);

    // When
    List<Book> result = bookRepository.findByAuthorName("Лев");

    // Then
    assertThat(result)
        .hasSize(2)
        .extracting(Book::getTitle)
        .containsExactlyInAnyOrder("Война и мир", "Анна Каренина");
  }

  @Test
  void findByTitleLike_shouldReturnBooksWithMatchingTitle() {
    // Given
    Author author = createValidAuthor("Александр", "Пушкин");
    createValidBook("Евгений Онегин", "Роман в стихах", author);
    createValidBook("Капитанская дочка", "Повесть", author);
    createValidBook("Медный всадник", "Поэма", author);

    // When
    List<Book> result = bookRepository.findByTitleLike("доЧка");

    // Then
    assertThat(result)
        .hasSize(1)
        .extracting(Book::getTitle)
        .containsExactly("Капитанская дочка");
  }

  @Test
  void findByGenre_shouldReturnBooksByGenre() {
    // Given
    Author author = createValidAuthor("Антон", "Чехов");
    createValidBook("Вишнёвый сад", "Пьеса", author);
    createValidBook("Чайка", "Пьеса", author);
    createValidBook("Палата №6", "Рассказ", author);

    // When
    List<Book> result = bookRepository.findByGenre("Пьеса");

    // Then
    assertThat(result)
        .hasSize(2)
        .extracting(Book::getTitle)
        .containsExactlyInAnyOrder("Вишнёвый сад", "Чайка");
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