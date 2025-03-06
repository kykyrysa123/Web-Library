package com.example.weblibrary.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval =
      true)
  private List<Book> books;
}
