package com.example.weblibrary.mapper;

import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between Book entities and DTOs. Provides
 * methods to map Book DTO requests to entities and entities to DTO responses.
 */
@Mapper(componentModel = "spring", uses = {AuthorMapper.class})
public interface BookMapper {

  /**
   * Converts a BookDtoRequest to a Book entity.
   *
   * @param bookDtoRequest the DTO containing book data to be mapped.
   * @return the corresponding Book entity.
   */
  Book toBookEntity(BookDtoRequest bookDtoRequest);

  /**
   * Converts a Book entity to a BookDtoResponse.
   *
   * @param book the Book entity to be mapped.
   * @return the corresponding BookDtoResponse DTO.
   */
  BookDtoResponse toBookDtoResponse(Book book);

  /**
   * Converts a list of Book entities to a list of BookDtoResponse objects.
   *
   * @param books the list of Book entities to be mapped.
   * @return the corresponding list of BookDtoResponse DTOs.
   */
  List<BookDtoResponse> toBookDtoResponse(List<Book> books);
}