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

/**
 * Service implementation for book-related operations.
 * Provides CRUD functionality and additional book search methods.
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements CrudService<BookDtoRequest, BookDtoResponse> {
  private static final String BOOK_NOT_FOUND_MESSAGE = "Книга не найдена с ID: ";
  private static final String AUTHOR_NOT_FOUND_MESSAGE = "Автор не найдена с ID: ";
  private final BookRepository bookRepository;
  private final BookMapperImpl bookMapper;
  private final AuthorRepository authorRepository;
  private final SimpleCache<Long, BookDtoResponse> bookCache;
  private final SimpleCache<String, List<BookDtoResponse>> bookListCache;
  private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

  @Override
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
  public BookDtoResponse getById(Long id) {
    BookDtoResponse cachedBook = bookCache.get(id);
    if (cachedBook != null) {
      log.info("Книга с ID={} получена из кэша.", id);
      return cachedBook;
    }
    log.info("Загрузка книги с ID={} из базы данных.", id);
    Book book = bookRepository.findById(id).orElseThrow(
        () -> new RuntimeException(BOOK_NOT_FOUND_MESSAGE + id));
    BookDtoResponse response = bookMapper.toBookDtoResponse(book);
    bookCache.put(id, response);
    return response;
  }

  @Override
  public BookDtoResponse create(BookDtoRequest bookDtoRequest) {
    Author author = authorRepository.findById(bookDtoRequest.authorId()).orElseThrow(
        () -> new RuntimeException(AUTHOR_NOT_FOUND_MESSAGE + bookDtoRequest.authorId()));
    Book book = bookMapper.toBookEntity(bookDtoRequest);
    book.setAuthor(author);
    Book savedBook = bookRepository.save(book);
    BookDtoResponse response = bookMapper.toBookDtoResponse(savedBook);
    bookCache.put(savedBook.getId(), response);
    bookListCache.clear();
    log.info("Создана новая книга с ID={}.", savedBook.getId());
    return response;
  }

  @Override
  public BookDtoResponse update(Long id, BookDtoRequest bookDtoRequest) {
    Book book = bookRepository.findById(id).orElseThrow(
        () -> new RuntimeException(BOOK_NOT_FOUND_MESSAGE + id));
    Author author = authorRepository.findById(bookDtoRequest.authorId()).orElseThrow(
        () -> new RuntimeException(AUTHOR_NOT_FOUND_MESSAGE + bookDtoRequest.authorId()));
    book.setTitle(bookDtoRequest.title());
    book.setGenre(bookDtoRequest.genre());
    book.setAuthor(author);
    Book updatedBook = bookRepository.save(book);
    BookDtoResponse response = bookMapper.toBookDtoResponse(updatedBook);
    bookCache.put(id, response);
    bookListCache.clear();
    log.info("Обновлена книга с ID={}.", id);
    return response;
  }

  @Override
  public void delete(Long id) {
    Book book = bookRepository.findById(id).orElseThrow(
        () -> new RuntimeException(BOOK_NOT_FOUND_MESSAGE + id));
    bookRepository.delete(book);
    bookCache.remove(id);
    bookListCache.clear();
    log.warn("Удалена книга с ID={}.", id);
  }

  /**
   * Retrieves books by their genre.
   *
   * @param genre the genre to search for
   * @return list of books matching the genre, or empty list if none found
   */
  public List<BookDtoResponse> getByGenre(String genre) {
    log.info("Поиск книг по жанру.");
    List<Book> books = bookRepository.findByGenre(genre);
    if (books.isEmpty()) {
      log.info("Книги с указанным жанром не найдены.");
      return Collections.emptyList();
    }
    return bookMapper.toBookDtoResponse(books);
  }

  /**
   * Retrieves books by author's name.
   *
   * @param authorName the name of the author to search for
   * @return list of books by the specified author, or empty list if none found
   */
  public List<BookDtoResponse> getBooksByAuthorName(String authorName) {
    log.info("Поиск книг по автору.");
    List<Book> books = bookRepository.findByAuthorName(authorName);
    if (books.isEmpty()) {
      log.info("Книги указанного автора не найдены.");
      return Collections.emptyList();
    }
    log.info("Найдено {} книг", books.size());
    return bookMapper.toBookDtoResponse(books);
  }

  /**
   * Retrieves books by title (partial match).
   *
   * @param title the title or part of title to search for
   * @return list of books matching the title, or empty list if none found
   */
  public List<BookDtoResponse> getBookByTitle(String title) {
    log.info("Выполняется поиск книг по названию.");
    List<Book> books = bookRepository.findByTitleLike(title);
    if (books.isEmpty()) {
      log.info("Книги с указанным названием не найдены.");
      return Collections.emptyList();
    }
    log.info("Найдено {} книг.", books.size());
    return bookMapper.toBookDtoResponse(books);
  }

  /**
   * Creates multiple books in a single operation.
   *
   * @param requests list of book creation requests
   * @return list of created book responses
   * @throws RuntimeException if any author is not found
   */
  public List<BookDtoResponse> createBooksBulk(List<BookDtoRequest> requests) {
    log.info("Создание {} книг (bulk-операция).", requests.size());
    List<Book> books = requests.stream().map(request -> {
      Author author = authorRepository.findById(request.authorId()).orElseThrow(
          () -> new RuntimeException(AUTHOR_NOT_FOUND_MESSAGE + request.authorId()));
      Book book = bookMapper.toBookEntity(request);
      book.setAuthor(author);
      return book;  
    }).toList();
    List<Book> savedBooks = bookRepository.saveAll(books);
    savedBooks.forEach(book ->
        bookCache.put(book.getId(), bookMapper.toBookDtoResponse(book)));
    bookListCache.clear();
    log.info("Успешно создано {} книг.", savedBooks.size());
    return bookMapper.toBookDtoResponse(savedBooks);
  }
}