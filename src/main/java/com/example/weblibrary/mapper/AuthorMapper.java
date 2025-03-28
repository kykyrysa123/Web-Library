package com.example.weblibrary.mapper;

import com.example.weblibrary.model.Author;
import com.example.weblibrary.model.dto.AuthorDtoRequest;
import com.example.weblibrary.model.dto.AuthorDtoResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper interface for converting between {@link Author} entities and their DTOs.
 * This interface uses MapStruct to generate the implementation at compile time.
 */
@Mapper(componentModel = "spring")
public interface AuthorMapper {

  /**
   * Converts an {@link AuthorDtoRequest} to an {@link Author} entity.
   *
   * @param authorDtoRequest the DTO containing author details.
   * @return the corresponding {@link Author} entity.
   */
  Author toAuthorEntity(AuthorDtoRequest authorDtoRequest);

  /**
   * Converts an {@link Author} entity to an {@link AuthorDtoResponse}.
   *
   * @param author the entity containing author details.
   * @return the corresponding {@link AuthorDtoResponse} DTO.
   */
  AuthorDtoResponse toAuthorDtoResponse(Author author);

  /**
   * Converts a list of {@link Author} entities to a list of {@link AuthorDtoResponse} objects.
   *
   * @param authors the list of entities containing author details.
   * @return the corresponding list of {@link AuthorDtoResponse} DTOs.
   */
  List<AuthorDtoResponse> toAuthorDtoResponse(List<Author> authors);

}