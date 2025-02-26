package com.example.weblibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents an entity class for [описание сущности, например, "storing user data"].
 * This class is used in [контекст, например, "the application's persistence layer"].
 */
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String title;

  private String publisher;

  private String isbn;

  private Long pages;

  private String genre;

  private LocalDate publishDate;

  private String language;

  private String description;

  private String imageUrl;


  // Геттеры
  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getPublisher() {
    return publisher;
  }

  public String getIsbn() {
    return isbn;
  }

  public Long getPages() {
    return pages;
  }

  public String getGenre() {
    return genre;
  }

  public LocalDate getPublishDate() {
    return publishDate;
  }

  public String getLanguage() {
    return language;
  }

  public String getDescription() {
    return description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  // Сеттеры
  public void setId(int id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public void setPages(Long pages) {
    this.pages = pages;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public void setPublishDate(LocalDate publishDate) {
    this.publishDate = publishDate;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

}
