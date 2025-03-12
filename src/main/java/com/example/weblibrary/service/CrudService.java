package com.example.weblibrary.service;

import java.util.List;

/**
 * Interface for CRUD (Create, Read, Update, Delete) operations on entities.
 *
 * @param <RequestT>  The type of request DTO.
 * @param <ResponseT> The type of response DTO.
 */
public interface CrudService<RequestT, ResponseT> {

  /**
   * Retrieves all entities.
   *
   * @return A list of all entities.
   */
  List<ResponseT> getAll();

  /**
   * Retrieves an entity by its ID.
   *
   * @param id The ID of the entity to retrieve.
   * @return The entity if found.
   */
  ResponseT getById(Long id);

  /**
   * Creates a new entity.
   *
   * @param entity The entity to be created.
   * @return The created entity.
   */
  ResponseT create(RequestT entity);

  /**
   * Updates an existing entity.
   *
   * @param id     The ID of the entity to update.
   * @param entity The updated entity data.
   * @return The updated entity.
   */
  ResponseT update(Long id, RequestT entity);

  /**
   * Deletes an entity by its ID.
   *
   * @param id The ID of the entity to delete.
   */
  void delete(Long id);
}
