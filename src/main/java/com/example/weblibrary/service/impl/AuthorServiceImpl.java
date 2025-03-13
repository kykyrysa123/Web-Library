package com.example.weblibrary.service.impl;

import com.example.weblibrary.mapper.AuthorMapperImpl;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.repository.AuthorRepository;
import com.example.weblibrary.service.CrudService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing author-related operations. Provides methods
 * to retrieve, create, update, and delete authors using the AuthorRepository.
 */
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements CrudService<AuthorDtoRequest, AuthorDtoResponse> {

  private static final String AUTHOR_NOT_FOUND = "Author not found with id: ";

  private final AuthorRepository authorRepository;
  private final AuthorMapperImpl authorMapper;

  @Override
  public List<AuthorDtoResponse> getAll() {
    List<Author> list = authorRepository.findAll();
    return authorMapper.toAuthorDtoResponse(list);
  }

  @Override
  public AuthorDtoResponse getById(Long id) {
    return authorMapper.toAuthorDtoResponse(
        authorRepository.findById(id).orElseThrow(() -> new RuntimeException(AUTHOR_NOT_FOUND + id)));
  }

  @Override
  public AuthorDtoResponse create(AuthorDtoRequest authorDtoRequest) {
    Author author = authorMapper.toAuthorEntity(authorDtoRequest);
    return authorMapper.toAuthorDtoResponse(authorRepository.save(author));
  }

  @Override
  @Transactional
  public AuthorDtoResponse update(Long id, AuthorDtoRequest authorDtoRequest) {
    authorRepository.findById(id).orElseThrow(
        () -> new RuntimeException(AUTHOR_NOT_FOUND + id));
    Author updatedAuthor = authorMapper.toAuthorEntity(authorDtoRequest);
    updatedAuthor.setId(id);

    return authorMapper.toAuthorDtoResponse(authorRepository.save(updatedAuthor));
  }

  @Override
  public void delete(Long id) {
    Author author = authorRepository.findById(id).orElseThrow(
        () -> new RuntimeException(AUTHOR_NOT_FOUND + id));
    authorRepository.delete(author);
  }

  /**
   * Retrieves an author along with their associated books by the author's ID.
   *
   * @param id the ID of the author to retrieve.
   * @return the AuthorDtoResponse containing author details and their books.
   * @throws RuntimeException if the author is not found.
   */
  @Transactional(readOnly = true)
  public AuthorDtoResponse getAuthorWithBooks(Long id) {
    Author author = authorRepository.findByIdWithBooks(id).orElseThrow(
        () -> new RuntimeException(AUTHOR_NOT_FOUND + id)
    );
    return authorMapper.toAuthorDtoResponse(author);
  }

  public Author saveAuthor(Author author) {
    return authorRepository.save(author);
  }
}