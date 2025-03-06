package com.example.weblibrary.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<E> {
  List<E> getAll();

  Optional<E> getById(int id);

  E create(E entity);

  E update(int id, E entity);

  void delete(int id);
}
