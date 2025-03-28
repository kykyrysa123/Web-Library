package com.example.weblibrary.service.impl;

import com.example.weblibrary.mapper.AuthorMapperImpl;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import com.example.weblibrary.repository.AuthorRepository;
import com.example.weblibrary.service.CrudService;
import com.example.weblibrary.service.cache.SimpleCache;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthorServiceImpl implements CrudService<AuthorDtoRequest, AuthorDtoResponse> {

  private static final String AUTHOR_NOT_FOUND = "Author not found with id: ";

  private final AuthorRepository authorRepository;
  private final AuthorMapperImpl authorMapper;
  private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

  private final SimpleCache<Long, AuthorDtoResponse> authorCache;
  private final SimpleCache<String, List<AuthorDtoResponse>> authorListCache;

  @Override
  public List<AuthorDtoResponse> getAll() {
    String cacheKey = "all_authors";

    List<AuthorDtoResponse> cachedList = authorListCache.get(cacheKey);
    if (cachedList != null) {
      logger.info("Author data loaded from cache (getAll)");
      return cachedList;
    }

    logger.info("Data for the author is loaded from the database (getAll)");
    List<Author> list = authorRepository.findAll();
    List<AuthorDtoResponse> responseList = authorMapper.toAuthorDtoResponse(list);

    authorListCache.put(cacheKey, responseList);
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
    Author author = authorRepository.findById(id).orElseThrow(
        () -> new RuntimeException(AUTHOR_NOT_FOUND + id));
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
    authorListCache.clear();
    return response;
  }

  @Override
  public AuthorDtoResponse update(Long id, AuthorDtoRequest authorDtoRequest) {
    Author existingAuthor = authorRepository.findById(id).orElseThrow(
        () -> new RuntimeException(AUTHOR_NOT_FOUND + id)
    );

    authorMapper.updateAuthorFromDto(authorDtoRequest, existingAuthor);
    AuthorDtoResponse response = authorMapper.toAuthorDtoResponse(
        authorRepository.save(existingAuthor));

    authorCache.put(id, response);
    authorListCache.clear();
    return response;
  }


  @Override
  public void delete(Long id) {
    Author author = authorRepository.findById(id).orElseThrow(
        () -> new RuntimeException(AUTHOR_NOT_FOUND + id));

    authorRepository.delete(author);
    authorCache.remove(id);
    authorListCache.clear();
  }

  @Transactional(readOnly = true)
  public AuthorDtoResponse getAuthorWithBooks(Long id) {
    Author author = authorRepository.findByIdWithBooks(id).orElseThrow(
        () -> new RuntimeException(AUTHOR_NOT_FOUND + id));
    return authorMapper.toAuthorDtoResponse(author);
  }

  public boolean existsById(Long id) {
    return authorRepository.existsById(id);
  }
}

