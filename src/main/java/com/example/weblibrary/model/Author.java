package com.example.weblibrary.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Class for Author book in libraryy.
 */
@Entity
@Table(name = "AUTHOR")
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String surname;

  private String patronymic;
  private LocalDate birthDate;
  private LocalDate deathDate;
  private String biography;
  private String genreSpecialization;
  private Double rating;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval =
      true, fetch = FetchType.LAZY)
  private List<Book> books = new ArrayList<>();

  public Author() {
  }

  public Author(Long id, String name, String surname, String patronymic,
      LocalDate birthDate, LocalDate deathDate, String biography,
      String genreSpecialization, Double rating
  ) {
    this.id = id;
    this.name = name;

    this.surname = surname;
    this.patronymic = patronymic;
    this.birthDate = birthDate;
    this.deathDate = deathDate;
    this.biography = biography;
    this.genreSpecialization = genreSpecialization;
    this.rating = rating;

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getPatronymic() {
    return patronymic;
  }

  public void setPatronymic(String patronymic) {
    this.patronymic = patronymic;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public LocalDate getDeathDate() {
    return deathDate;
  }

  public void setDeathDate(LocalDate deathDate) {
    this.deathDate = deathDate;
  }

  public String getBiography() {
    return biography;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public String getGenreSpecialization() {
    return genreSpecialization;
  }

  public void setGenreSpecialization(String genreSpecialization) {
    this.genreSpecialization = genreSpecialization;
  }

  public Double getRating() {
    return rating;
  }

  public void setRating(Double rating) {
    this.rating = rating;
  }

  public List<Book> getBooks() {
    return books;
  }

  public void setBooks(List<Book> books) {
    this.books = books;
  }
}
