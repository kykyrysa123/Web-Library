package com.example.weblibrary.repository;

import com.example.weblibrary.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Book entities.
 * Extends JpaRepository to provide basic CRUD operations and custom query methods.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
  /**
   * Retrieves a list of books by their genre.
   *
   * @param genre the genre to filter books by
   * @return a list of books matching the specified genre
   */
  List<Book> findByGenre(String genre);
  // You can add custom query methods here if needed
  // For example:
  // List<Book> findByTitleContaining(String title);
}