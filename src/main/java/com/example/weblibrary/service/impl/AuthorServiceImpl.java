package com.example.weblibrary.service.impl;

import com.example.weblibrary.mapper.AuthorMapperImpl;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.repository.AuthorRepository;
import com.example.weblibrary.service.CrudService;
import com.example.weblibrary.service.cache.SimpleCache;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing author-related operations. Provides
 * methods to retrieve, create, update, and delete authors using the
 * AuthorRepository.
 */
@Service
public class AuthorServiceImpl implements CrudService<AuthorDtoRequest, AuthorDtoResponse> {
  /**
  constructor.
 */
  public AuthorServiceImpl(AuthorRepository authorRepository,
      AuthorMapperImpl authorMapper
  ) {
    this.authorRepository = authorRepository;
    this.authorMapper = authorMapper;
  }

  private static final String AUTHOR_NOT_FOUND = "Author not found with id: ";

  private final AuthorRepository authorRepository;
  private final AuthorMapperImpl authorMapper;
  private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

  private final SimpleCache<Long, AuthorDtoResponse> authorCache = new SimpleCache<>(100);
  private final SimpleCache<String, List<AuthorDtoResponse>> authorCache1 = new SimpleCache<>(100);

  @Override
  public List<AuthorDtoResponse> getAll() {
    String cacheKey = "all_authors";

    List<AuthorDtoResponse> cachedList = authorCache1.get(cacheKey);
    if (cachedList != null) {
      logger.info("Author data loaded from cache (getAll)");
      return cachedList;
    }

    logger.info("Data for the author is loaded from the database (getAll)");
    List<Author> list = authorRepository.findAll();
    List<AuthorDtoResponse> responseList = authorMapper.toAuthorDtoResponse(list);

    authorCache1.put(cacheKey, responseList);
    return responseList;
  }

  @Override
  public AuthorDtoResponse getById(Long id) {
    AuthorDtoResponse cachedAuthor = authorCache.get(id);
    if (cachedAuthor != null) {
      logger.info("Author with id={} retrieved from cache", id);
      return cachedAuthor;
    }

    logger.info("The author with id={} is loaded from the database", id);
    Author author = authorRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException(AUTHOR_NOT_FOUND + id));
    AuthorDtoResponse authorDtoResponse = authorMapper.toAuthorDtoResponse(author);

    authorCache.put(id, authorDtoResponse);
    return authorDtoResponse;
  }

  @Override
  public AuthorDtoResponse create(AuthorDtoRequest authorDtoRequest) {
    Author author = authorMapper.toAuthorEntity(authorDtoRequest);
    Author savedAuthor = authorRepository.save(author);
    AuthorDtoResponse response = authorMapper.toAuthorDtoResponse(savedAuthor);

    authorCache.put(savedAuthor.getId(), response);
    authorCache1.clear(); // Clear the cache containing all authors list
    return response;
  }

  @Override
  public AuthorDtoResponse update(Long id, AuthorDtoRequest authorDtoRequest) {
    authorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException(AUTHOR_NOT_FOUND + id));
    Author updatedAuthor = authorMapper.toAuthorEntity(authorDtoRequest);
    updatedAuthor.setId(id);
    AuthorDtoResponse response = authorMapper.toAuthorDtoResponse(
        authorRepository.save(updatedAuthor));

    authorCache.put(id, response);
    authorCache1.clear(); // Clear the cache containing all authors list
    return response;
  }

  @Override
  public void delete(Long id) {
    Author author = authorRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException(AUTHOR_NOT_FOUND + id));

    authorRepository.delete(author);
    authorCache.remove(id);
    authorCache1.clear(); // Clear the cache containing all authors list
  }

  /**
   * Retrieves an author along with their associated books by the author's ID.
   *
   * @param id the ID of the author to retrieve
   * @return the AuthorDtoResponse containing author details and their books
   * @throws RuntimeException if the author is not found
   */
  @Transactional(readOnly = true)
  public AuthorDtoResponse getAuthorWithBooks(Long id) {
    Author author = authorRepository.findByIdWithBooks(id)
                                    .orElseThrow(() -> new RuntimeException(AUTHOR_NOT_FOUND + id));
    return authorMapper.toAuthorDtoResponse(author);
  }

  /**
   * Checks if an author exists with the given ID.
   *
   * @param id the ID of the author to check
   * @return true if the author exists, false otherwise
   */
  public boolean existsById(Long id) {
    return authorRepository.existsById(id);
  }
}