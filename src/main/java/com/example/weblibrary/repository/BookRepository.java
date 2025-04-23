package com.example.weblibrary.repository;

import com.example.weblibrary.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для управления сущностями Book.
 * Расширяет JpaRepository, предоставляя базовые CRUD-операции и кастомные запросы.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  /**
   * Находит книги по имени автора.
   * Учитывает связь многие-ко-многим через таблицу book_authors.
   *
   * @param authorName имя автора, по которому осуществляется поиск
   * @return список книг, написанных указанным автором
   */
  @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.name = :authorName")
  List<Book> findByAuthorName(@Param("authorName") String authorName);

  /**
   * Находит книги, заголовок которых содержит указанную строку (без учета регистра).
   *
   * @param title часть заголовка книги для поиска
   * @return список книг с заголовками, содержащими указанную строку
   */
  List<Book> findByTitleContainingIgnoreCase(@Param("title") String title);

  /**
   * Находит книги по жанру.
   *
   * @param genre жанр книги
   * @return список книг, относящихся к указанному жанру
   */
  List<Book> findByGenre(String genre);
}