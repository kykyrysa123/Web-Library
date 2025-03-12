package com.example.weblibrary.repository;

import com.example.weblibrary.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Book entities.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

  /**
   * Retrieves a list of books by their genre.
   *
   * @param genre the genre to filter books by
   * @return a list of books matching the specified genre
   */
  List<Book> findByGenre(String genre);

  /**
   * Finds books by genre with pagination.
   *
   * @param genre the genre to filter books by
   * @param pageable pagination details
   * @return a page of books matching the specified genre
   */
  Page<Book> findByGenre(String genre, Pageable pageable);

  /**
   * Finds a book by ID along with its author (uses JOIN FETCH to avoid N+1 problem).
   *
   * @param id the ID of the book
   * @return the book with its author
   */
  @Query("SELECT b FROM Book b JOIN FETCH b.author WHERE b.id = :id")
  Optional<Book> findByIdWithAuthor(@Param("id") Long id);

  /**
   * Finds all books along with their authors (uses JOIN FETCH to avoid N+1 problem).
   *
   * @return a list of books with their authors
   */
  @Query("SELECT DISTINCT b FROM Book b JOIN FETCH b.author")
  List<Book> findAllWithAuthors();

  /**
   * Finds books by title (case-insensitive).
   *
   * @param title the title of the book
   * @return a list of books with the specified title
   */
  List<Book> findByTitleContainingIgnoreCase(String title);

  /**
   * Finds books by language (case-insensitive).
   *
   * @param language the language of the book
   * @return a list of books with the specified language
   */
  List<Book> findByLanguageContainingIgnoreCase(String language);

  /**
   * Finds books by genre and language (case-insensitive).
   *
   * @param genre the genre of the book
   * @param language the language of the book
   * @return a list of books with the specified genre and language
   */
  List<Book> findByGenreAndLanguageContainingIgnoreCase(String genre, String language);

  /**
   * Finds books with a rating greater than or equal to the specified value.
   *
   * @param rating the minimum rating
   * @return a list of books with a rating greater than or equal to the specified value
   */
  List<Book> findByRatingGreaterThanEqual(Double rating);

  /**
   * Finds books with a rating less than or equal to the specified value.
   *
   * @param rating the maximum rating
   * @return a list of books with a rating less than or equal to the specified value
   */
  List<Book> findByRatingLessThanEqual(Double rating);
}
