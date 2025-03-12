package com.example.weblibrary.repository;

import com.example.weblibrary.model.Author;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing authors in the database.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

  /**
   * Finds an author by ID along with their books (uses JOIN FETCH to avoid N+1 problem).
   *
   * @param id The ID of the author.
   * @return The author with their books.
   */
  @Query("SELECT a FROM Author a JOIN FETCH a.books WHERE a.id = :id")
  Optional<Author> findByIdWithBooks(@Param("id") Long id);

  /**
   * Finds all authors along with their books (uses JOIN FETCH to avoid N+1 problem).
   *
   * @return A list of authors with their books.
   */
  @Query("SELECT DISTINCT a FROM Author a JOIN FETCH a.books")
  List<Author> findAllWithBooks();

  /**
   * Finds authors by name (case-insensitive).
   *
   * @param name The author's name.
   * @return A list of authors with the given name.
   */
  List<Author> findByNameContainingIgnoreCase(String name);

  /**
   * Finds authors by surname (case-insensitive).
   *
   * @param surname The author's surname.
   * @return A list of authors with the given surname.
   */
  List<Author> findBySurnameContainingIgnoreCase(String surname);

  /**
   * Finds authors by both name and surname (case-insensitive).
   *
   * @param name    The author's name.
   * @param surname The author's surname.
   * @return A list of authors with the given name and surname.
   */
  List<Author> findByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(
      String name, String surname
  );
}
