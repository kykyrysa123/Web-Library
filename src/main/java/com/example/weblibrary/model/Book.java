package com.example.weblibrary.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

/**
 * Класс, представляющий книгу в системе библиотеки.
 */
@Entity
@Table(name = "BOOK")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  @Column(nullable = false)
  private String title;

  @NonNull
  @Column(nullable = false)
  private String publisher;

  @Column(unique = true)
  private String isbn;

  private Integer pages;

  @NonNull
  @Column(nullable = false)
  private String genre;

  private LocalDate publishDate;

  @NonNull
  @Column(nullable = false)
  private String language;

  @Column(length = 1000)
  private String description;

  private String imageUrl;

  private Double rating;

  private String readUrl;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  @JoinTable(
      name = "book_authors",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "author_id")
  )
  private List<Author> authors = new ArrayList<>();

  public Book() {
  }

  public Book(Long id, @NonNull String title, @NonNull String publisher,
      String isbn, Integer pages, @NonNull String genre, LocalDate publishDate,
      @NonNull String language, String description, String imageUrl, String readUrl,
      Double rating) {
    this.id = id;
    this.title = title;
    this.publisher = publisher;
    this.isbn = isbn;
    this.pages = pages;
    this.genre = genre;
    this.publishDate = publishDate;
    this.language = language;
    this.description = description;
    this.imageUrl = imageUrl;
    this.readUrl = readUrl;
    this.rating = rating;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public @NonNull String getTitle() {
    return title;
  }

  public void setTitle(@NonNull String title) {
    this.title = title;
  }

  public @NonNull String getPublisher() {
    return publisher;
  }

  public void setPublisher(@NonNull String publisher) {
    this.publisher = publisher;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public Integer getPages() {
    return pages;
  }

  public void setPages(Integer pages) {
    this.pages = pages;
  }

  public @NonNull String getGenre() {
    return genre;
  }

  public void setGenre(@NonNull String genre) {
    this.genre = genre;
  }

  public LocalDate getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(LocalDate publishDate) {
    this.publishDate = publishDate;
  }

  public @NonNull String getLanguage() {
    return language;
  }

  public void setLanguage(@NonNull String language) {
    this.language = language;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Double getRating() {
    return rating;
  }

  public void setRating(Double rating) {
    this.rating = rating;
  }

  public String getReadUrl() {
    return readUrl;
  }

  public void setReadUrl(String readUrl) {
    this.readUrl = readUrl;
  }

  public List<Author> getAuthors() {
    return authors;
  }

  public void setAuthors(List<Author> authors) {
    this.authors = authors;
  }
}