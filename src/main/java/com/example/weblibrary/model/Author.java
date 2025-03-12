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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class for Author book in library.
 */
@Entity
@Table(name = "author")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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


  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public String getSurname() {
    return surname;
  }

  public String getPatronymic() {
    return patronymic;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public String getBiography() {
    return biography;
  }

  public LocalDate getDeathDate() {
    return deathDate;
  }

  public Double getRating() {
    return rating;
  }

  public List<Book> getBooks() {
    return books;
  }

  public String getGenreSpecialization() {
    return genreSpecialization;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public void setPatronymic(String patronymic) {
    this.patronymic = patronymic;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public void setDeathDate(LocalDate deathDate) {
    this.deathDate = deathDate;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public void setGenreSpecialization(String genreSpecialization) {
    this.genreSpecialization = genreSpecialization;
  }

  public void setRating(Double rating) {
    this.rating = rating;
  }

  public void setBooks(List<Book> books) {
    this.books = books;
  }
}
