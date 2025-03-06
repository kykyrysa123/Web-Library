package com.example.weblibrary.service.impl;

import com.example.weblibrary.model.Author;
import com.example.weblibrary.repository.AuthorRepository;
import com.example.weblibrary.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements CrudService<Author> {

  private final AuthorRepository authorRepository;

  public AuthorServiceImpl(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  @Override
  public List<Author> getAll() {
    return authorRepository.findAll();
  }

  @Override
  public Optional<Author> getById(int id) {
    return authorRepository.findById(id);
  }

  @Override
  public Author create(Author author) {
    return authorRepository.save(author);
  }

  @Override
  public Author update(int id, Author author) {
    if (authorRepository.existsById(id)) {
      author.setId(id);
      return authorRepository.save(author);
    } else {
      throw new RuntimeException("Author not found with id: " + id);
    }
  }

  @Override
  public void delete(int id) {
    if (authorRepository.existsById(id)) {
      authorRepository.deleteById(id);
    } else {
      throw new RuntimeException("Author not found with id: " + id);
    }
  }
}
