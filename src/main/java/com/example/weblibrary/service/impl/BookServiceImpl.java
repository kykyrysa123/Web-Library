package com.example.weblibrary.service.impl;

import com.example.weblibrary.mapper.BookMapperImpl;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.repository.AuthorRepository;
import com.example.weblibrary.repository.BookRepository;
import com.example.weblibrary.service.CrudService;
import com.example.weblibrary.service.cache.SimpleCache;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for book-related operations.
 * Provides CRUD functionality and additional book search methods.
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements CrudService<BookDtoRequest, BookDtoResponse> {
  private static final String BOOK_NOT_FOUND_MESSAGE = "Книга не найдена с ID: ";
  private static final String AUTHOR_NOT_FOUND_MESSAGE = "Один или несколько авторов не найдены";
  private final BookRepository bookRepository;
  private final BookMapperImpl bookMapper;
  private final AuthorRepository authorRepository;
  private final SimpleCache<Long, BookDtoResponse> bookCache;
  private final SimpleCache<String, List<BookDtoResponse>> bookListCache;
  private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

  @Override
  @Transactional(readOnly = true)
  public List<BookDtoResponse> getAll() {
    String cacheKey = "all_books";
    List<BookDtoResponse> cachedList = bookListCache.get(cacheKey);
    if (cachedList != null) {
      log.info("Получены все книги из кэша.");
      return cachedList;
    }
    log.info("Загрузка всех книг из базы данных.");
    List<BookDtoResponse> responseList = bookMapper.toBookDtoResponse(bookRepository.findAll());
    bookListCache.put(cacheKey, responseList);
    return responseList;
  }

  @Override
  @Transactional(readOnly = true)
  public BookDtoResponse getById(Long id) {
    BookDtoResponse cachedBook = bookCache.get(id);
    if (cachedBook != null) {
      log.info("Книга с ID={} получена из кэша.", id);
      return cachedBook;
    }
    log.info("Загрузка книги с ID={} из базы данных.", id);
    Book book = bookRepository.findById(id)
                              .orElseThrow(() -> new RuntimeException(BOOK_NOT_FOUND_MESSAGE + id));
    BookDtoResponse response = bookMapper.toBookDtoResponse(book);
    bookCache.put(id, response);
    return response;
  }

  @Override
  @Transactional
  public BookDtoResponse create(BookDtoRequest bookDtoRequest) {
    log.info("Создание книги с authorIds: {}", bookDtoRequest.authorIds());
    if (bookDtoRequest.authorIds() == null || bookDtoRequest.authorIds().isEmpty()) {
      throw new IllegalArgumentException("Необходимо указать хотя бы одного автора");
    }
    List<Author> authors = authorRepository.findAllById(bookDtoRequest.authorIds());
    if (authors.size() != bookDtoRequest.authorIds().size()) {
      throw new RuntimeException(AUTHOR_NOT_FOUND_MESSAGE);
    }
    Book book = bookMapper.toBookEntity(bookDtoRequest);
    book.setAuthors(authors);
    Book savedBook = bookRepository.save(book);
    BookDtoResponse response = bookMapper.toBookDtoResponse(savedBook);
    bookCache.put(savedBook.getId(), response);
    bookListCache.clear();
    log.info("Создана новая книга с ID={}.", savedBook.getId());
    return response;
  }

  @Override
  @Transactional
  public BookDtoResponse update(Long id, BookDtoRequest bookDtoRequest) {
    log.info("Обновление книги с ID: {}", id);
    Book book = bookRepository.findById(id)
                              .orElseThrow(() -> new RuntimeException(BOOK_NOT_FOUND_MESSAGE + id));
    if (bookDtoRequest.authorIds() == null || bookDtoRequest.authorIds().isEmpty()) {
      throw new IllegalArgumentException("Необходимо указать хотя бы одного автора");
    }
    List<Author> authors = authorRepository.findAllById(bookDtoRequest.authorIds());
    if (authors.size() != bookDtoRequest.authorIds().size()) {
      throw new RuntimeException(AUTHOR_NOT_FOUND_MESSAGE);
    }
    Book updatedBook = bookMapper.toBookEntity(bookDtoRequest);
    updatedBook.setId(id);
    updatedBook.setAuthors(authors);
    Book savedBook = bookRepository.save(updatedBook);
    BookDtoResponse response = bookMapper.toBookDtoResponse(savedBook);
    bookCache.put(id, response);
    bookListCache.clear();
    log.info("Обновлена книга с ID={}.", id);
    return response;
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.warn("Удаление книги с ID: {}", id);
    Book book = bookRepository.findById(id)
                              .orElseThrow(() -> new RuntimeException(BOOK_NOT_FOUND_MESSAGE + id));
    bookRepository.delete(book);
    bookCache.remove(id);
    bookListCache.clear();
    log.warn("Удалена книга с ID={}.", id);
  }

  @Transactional(readOnly = true)
  public List<BookDtoResponse> getByGenre(String genre) {
    log.info("Поиск книг по жанру: {}", genre);
    List<Book> books = bookRepository.findByGenre(genre);
    if (books.isEmpty()) {
      log.info("Книги с жанром {} не найдены.", genre);
      return Collections.emptyList();
    }
    return bookMapper.toBookDtoResponse(books);
  }

  @Transactional(readOnly = true)
  public List<BookDtoResponse> getBookByTitle(String title) {
    log.info("Поиск книг по названию: {}", title);
    List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
    if (books.isEmpty()) {
      log.info("Книги с названием {} не найдены.", title);
      return Collections.emptyList();
    }
    log.info("Найдено {} книг.", books.size());
    return bookMapper.toBookDtoResponse(books);
  }

  @Transactional
  public List<BookDtoResponse> createBooksBulk(List<BookDtoRequest> requests) {
    log.info("Создание {} книг (bulk-операция).", requests.size());
    List<Book> books = requests.stream().map(request -> {
      if (request.authorIds() == null || request.authorIds().isEmpty()) {
        throw new IllegalArgumentException("Необходимо указать хотя бы одного автора");
      }
      List<Author> authors = authorRepository.findAllById(request.authorIds());
      if (authors.size() != request.authorIds().size()) {
        throw new RuntimeException(AUTHOR_NOT_FOUND_MESSAGE);
      }
      Book book = bookMapper.toBookEntity(request);
      book.setAuthors(authors);
      return book;
    }).toList();
    List<Book> savedBooks = bookRepository.saveAll(books);
    List<BookDtoResponse> responses = bookMapper.toBookDtoResponse(savedBooks);
    savedBooks.forEach(book ->
        bookCache.put(book.getId(), bookMapper.toBookDtoResponse(book)));
    bookListCache.clear();
    log.info("Успешно создано {} книг.", savedBooks.size());
    return responses;
  }
}