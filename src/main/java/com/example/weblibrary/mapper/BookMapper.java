package com.example.weblibrary.mapper;

import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Book entities and DTOs.
 */
@Mapper(componentModel = "spring", uses = {AuthorMapper.class})
public interface BookMapper {

  /**
   * Converts a BookDtoRequest to a Book entity.
   * Note: authorIds are not mapped directly; authors are set in the service.
   */
  @Mapping(target = "authors", ignore = true)
  Book toBookEntity(BookDtoRequest bookDtoRequest);

  /**
   * Converts a Book entity to a BookDtoResponse.
   */
  BookDtoResponse toBookDtoResponse(Book book);

  /**
   * Converts a list of Book entities to a list of BookDtoResponse objects.
   */
  List<BookDtoResponse> toBookDtoResponse(List<Book> books);
}