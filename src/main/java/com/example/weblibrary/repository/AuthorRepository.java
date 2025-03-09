package com.example.weblibrary.repository;

import com.example.weblibrary.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing authors in the database.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
