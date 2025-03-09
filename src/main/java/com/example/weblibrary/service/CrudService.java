package com.example.weblibrary.service;

import java.util.List;
import java.util.Optional;

/**
 * Interface for CRUD (Create, Read, Update, Delete) operations on entities.
 *
 * @param <E> The type of entity the service will manage.
 */
public interface CrudService<E> {

  /**
   * Retrieves all entities.
   *
   * @return A list of all entities.
   */
  List<E> getAll();

  /**
   * Retrieves an entity by its ID.
   *
   * @param id The ID of the entity to retrieve.
   * @return An Optional containing the entity if found, or empty if not found.
   */
  Optional<E> getById(int id);

  /**
   * Creates a new entity.
   *
   * @param entity The entity to be created.
   * @return The created entity.
   */
  E create(E entity);

  /**
   * Updates an existing entity.
   *
   * @param id The ID of the entity to update.
   * @param entity The updated entity data.
   * @return The updated entity.
   */
  E update(int id, E entity);

  /**
   * Deletes an entity by its ID.
   *
   * @param id The ID of the entity to delete.
   */
  void delete(int id);
}
