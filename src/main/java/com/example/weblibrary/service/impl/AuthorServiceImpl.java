package com.example.weblibrary.service.impl;

import com.example.weblibrary.model.Author;
import com.example.weblibrary.repository.AuthorRepository;
import com.example.weblibrary.service.CrudService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Implementation of CRUD operations for authors.
 */
@Service
public class AuthorServiceImpl implements CrudService<Author> {

  private final AuthorRepository authorRepository;

  /**
   * Constructor for AuthorServiceImpl.
   *
   * @param authorRepository repository for managing authors
   */
  public AuthorServiceImpl(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  /**
   * Retrieve all authors.
   *
   * @return list of authors
   */
  @Override
  public List<Author> getAll() {
    return authorRepository.findAll();
  }

  /**
   * Retrieve an author by ID.
   *
   * @param id author ID
   * @return optional author
   */
  @Override
  public Optional<Author> getById(int id) {
    return authorRepository.findById(id);
  }

  /**
   * Create a new author.
   *
   * @param author author object
   * @return created author
   */
  @Override
  public Author create(Author author) {
    return authorRepository.save(author);
  }

  /**
   * Update an existing author.
   *
   * @param id author ID
   * @param author updated author data
   * @return updated author
   * @throws RuntimeException if author is not found
   */
  @Override
  public Author update(int id, Author author) {
    if (authorRepository.existsById(id)) {
      author.setId(id);
      return authorRepository.save(author);
    } else {
      throw new RuntimeException("Author not found with id: " + id);
    }
  }

  /**
   * Delete an author by ID.
   *
   * @param id author ID
   * @throws RuntimeException if author is not found
   */
  @Override
  public void delete(int id) {
    if (authorRepository.existsById(id)) {
      authorRepository.deleteById(id);
    } else {
      throw new RuntimeException("Author not found with id: " + id);
    }
  }
}
