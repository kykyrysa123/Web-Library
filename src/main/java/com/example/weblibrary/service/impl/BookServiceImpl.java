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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Реализация сервиса для управления книгами.
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements CrudService<BookDtoRequest,
    BookDtoResponse> {

  private final BookRepository bookRepository;
  private final BookMapperImpl bookMapper;
  private final AuthorRepository authorRepository;

  private final SimpleCache<Long, BookDtoResponse> bookCache =
      new SimpleCache<>(
      100);
  private final SimpleCache<String, List<BookDtoResponse>> bookListCache =
      new SimpleCache<>(
      100);

  private static final Logger log = LoggerFactory.getLogger(
      BookServiceImpl.class);

  @Override
  public List<BookDtoResponse> getAll() {
    String cacheKey = "all_books";
    List<BookDtoResponse> cachedList = bookListCache.get(cacheKey);

    if (cachedList != null) {
      log.info("Получены все книги из кэша.");
      return cachedList;
    }

    log.info("Загрузка всех книг из базы данных.");
    List<BookDtoResponse> responseList = bookMapper.toBookDtoResponse(
        bookRepository.findAll());
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
        () -> new RuntimeException("Книга не найдена с ID: " + id));
    BookDtoResponse response = bookMapper.toBookDtoResponse(book);

    bookCache.put(id, response);
    return response;
  }

  @Override
  public BookDtoResponse create(BookDtoRequest bookDtoRequest) {
    Author author = authorRepository.findById(
        bookDtoRequest.authorId()).orElseThrow(() -> new RuntimeException(
        "Автор не найден с ID: " + bookDtoRequest.authorId()));

    Book book = bookMapper.toBookEntity(bookDtoRequest);
    book.setAuthor(author);
    Book savedBook = bookRepository.save(book);

    BookDtoResponse response = bookMapper.toBookDtoResponse(savedBook);
    bookCache.put(savedBook.getId(), response);
    bookListCache.clear(); // Очистка кэша списка книг

    log.info("Создана новая книга с ID={}.", savedBook.getId());
    return response;
  }

  @Override
  public BookDtoResponse update(Long id, BookDtoRequest bookDtoRequest) {
    Book book = bookRepository.findById(id).orElseThrow(
        () -> new RuntimeException("Книга не найдена с ID: " + id));

    Author author = authorRepository.findById(
        bookDtoRequest.authorId()).orElseThrow(() -> new RuntimeException(
        "Автор не найден с ID: " + bookDtoRequest.authorId()));

    book.setTitle(bookDtoRequest.title());
    book.setGenre(bookDtoRequest.genre());
    book.setAuthor(author);

    Book updatedBook = bookRepository.save(book);
    BookDtoResponse response = bookMapper.toBookDtoResponse(updatedBook);

    bookCache.put(id, response);
    bookListCache.clear(); // Очистка кэша списка книг

    log.info("Обновлена книга с ID={}.", id);
    return response;
  }

  @Override
  public void delete(Long id) {
    Book book = bookRepository.findById(id).orElseThrow(
        () -> new RuntimeException("Книга не найдена с ID: " + id));

    bookRepository.delete(book);
    bookCache.remove(id);
    bookListCache.clear(); // Очистка кэша списка книг

    log.warn("Удалена книга с ID={}.", id);
  }

  /**
   * Получает список книг по жанру.
   *
   * @param genre
   *     жанр книги
   * @return список книг указанного жанра
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
   * Получает список книг по имени автора.
   *
   * @param authorName
   *     имя автора
   * @return список книг данного автора
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
   * Получает список книг, название которых содержит указанную строку.
   *
   * @param title
   *     часть названия книги
   * @return список книг, содержащих указанное название
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
   * Создаёт несколько книг за один запрос.
   *
   * @param requests
   *     список книг для создания
   * @return список созданных книг
   */
  public List<BookDtoResponse> createBooksBulk(List<BookDtoRequest> requests) {
    log.info("Создание {} книг (bulk-операция).", requests.size());

    List<Book> books = requests.stream().map(request -> {
      Author author = authorRepository.findById(request.authorId()).orElseThrow(
          () -> new RuntimeException(
              "Автор не найден с ID: " + request.authorId()));

      Book book = bookMapper.toBookEntity(request);
      book.setAuthor(author);
      return book;
    }).toList(); // Java 16+: заменяет collect(Collectors.toList())

    List<Book> savedBooks = bookRepository.saveAll(
        books); // Массовое сохранение
    List<BookDtoResponse> responses = bookMapper.toBookDtoResponse(savedBooks);

    // Добавляем созданные книги в кэш
    savedBooks.forEach(book -> bookCache.put(book.getId(),
        bookMapper.toBookDtoResponse(book)));

    bookListCache.clear(); // Очистка кэша списка книг
    log.info("Успешно создано {} книг.", savedBooks.size());

    return responses;
  }
}
