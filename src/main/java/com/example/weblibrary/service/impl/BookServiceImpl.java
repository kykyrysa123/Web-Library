package com.example.weblibrary.service.impl;

import com.example.weblibrary.mapper.BookMapperImpl;
import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.repository.AuthorRepository;
import com.example.weblibrary.repository.BookRepository;
import com.example.weblibrary.service.CrudService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing book-related operations. Provides methods
 * to retrieve, create, update, and delete books using the BookRepository.
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements
    CrudService<BookDtoRequest, BookDtoResponse> {

  private final BookRepository bookRepository;
  private final BookMapperImpl bookMapper;
  private final AuthorRepository authorRepository;

  @Override
  public List<BookDtoResponse> getAll() {
    return bookMapper.toBookDtoResponse(bookRepository.findAll());
  }

  @Override
  public BookDtoResponse getById(Long id) {
    return bookMapper.toBookDtoResponse(
        bookRepository.findById(id)
                      .orElseThrow(NullPointerException::new));
  }

  @Override
  public BookDtoResponse create(BookDtoRequest bookDtoRequest) {
    Author author = authorRepository.findById(bookDtoRequest.authorId())
                                    .orElseThrow(NullPointerException::new);

    Book book = bookMapper.toBookEntity(bookDtoRequest);
    book.setAuthor(author);

    return bookMapper.toBookDtoResponse(bookRepository.save(book));
  }

  @Override
  public BookDtoResponse update(Long id, BookDtoRequest bookDtoRequest) {
    Book savedBook = bookRepository.findById(id).orElseThrow(
        () -> new RuntimeException("Book not found with id: " + id));

    Book updateBook = bookMapper.toBookEntity(bookDtoRequest);
    updateBook.setId(id);
    updateBook.setAuthor(savedBook.getAuthor());

    return bookMapper.toBookDtoResponse(bookRepository.save(updateBook));
  }

  @Override
  public void delete(Long id) {
    Book book = bookRepository.findById(id).orElseThrow(
        () -> new RuntimeException("Book not found with id: " + id));
    bookRepository.delete(book);
  }

  /**
   * Retrieves books by genre.
   *
   * @param genre the genre of books to filter by
   * @return a list of books matching the specified genre
   */
  public List<BookDtoResponse> getByGenre(String genre) {
    return bookMapper.toBookDtoResponse(bookRepository.findByGenre(genre));
  }
}
