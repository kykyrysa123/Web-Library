package com.example.weblibrary.repository;

import com.example.weblibrary.model.Author;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing authors and books in the database.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

  /**
   * Finds an author by ID along with their books (uses EntityGraph to avoid N+1 problem).
   *
   * @param id The ID of the author.
   * @return The author with their books.
   */
  @EntityGraph(attributePaths = {"books"})
  @Query("SELECT a FROM Author a JOIN FETCH a.books WHERE a.id = :id")
  Optional<Author> findByIdWithBooks(@Param("id") Long id);
  List<Author> findByNameContainingIgnoreCase(String name);
}