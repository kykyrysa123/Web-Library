package com.example.weblibrary.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий автора книг в библиотеке.
 */
@Entity
@Table(name = "author")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NonNull
  private String name;

  @NonNull
  private String surname;

  private String patronymic;
  private LocalDate birthDate;
  private String deathDate;
  private String biography;
  private String genreSpecialization;
  private Double rating;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Book> books;
}