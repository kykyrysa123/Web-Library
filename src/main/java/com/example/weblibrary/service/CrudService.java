package com.example.weblibrary.service;

import java.util.List;

/**
 * Interface for CRUD (Create, Read, Update, Delete) operations on entities.
 *
 * @param <Q>
 *     The type of request DTO.
 * @param <T>
 *     The type of response DTO.
 */
public interface CrudService<Q, T> {

  /**
   * Retrieves all entities.
   *
   * @return A list of all entities.
   */
  List<T> getAll();

  /**
   * Retrieves an entity by its ID.
   *
   * @param id
   *     The ID of the entity to retrieve.
   * @return The entity if found.
   */
  T getById(Long id);

  /**
   * Creates a new entity.
   *
   * @param entity
   *     The entity to be created.
   * @return The created entity.
   */
  T create(Q entity);

  /**
   * Updates an existing entity.
   *
   * @param id
   *     The ID of the entity to update.
   * @param entity
   *     The updated entity data.
   * @return The updated entity.
   */
  T update(Long id, Q entity);

  /**
   * Deletes an entity by its ID.
   *
   * @param id
   *     The ID of the entity to delete.
   */
  void delete(Long id);
}
