package com.example.weblibrary.mapper;

import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;

/**
 * Mapper interface for converting between Book entities and DTOs.
 * Provides methods to map Book DTO requests to entities and entities to DTO responses.
 */
public interface BookMapper {
  /**
   * Converts a BookDtoRequest to a Book entity.
   *
   * @param bookDtoRequest the DTO containing book data to be mapped
   * @return the corresponding Book entity
   */
  Book toBookEntity(BookDtoRequest bookDtoRequest);

  /**
   * Converts a Book entity to a BookDtoResponse.
   *
   * @param book the Book entity to be mapped
   * @return the corresponding BookDtoResponse DTO
   */
  BookDtoResponse toBookDtoResponse(Book book);
}