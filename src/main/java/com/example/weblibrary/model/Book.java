package com.example.weblibrary.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
 * Represents a Book entity in the web library system.
 */
@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
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

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "author_id", nullable = false)
  private Author author;

  @OneToMany(mappedBy = "book", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> review;

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

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
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

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public List<Review> getReview() {
    return review;
  }

  public void setReview(List<Review> review) {
    this.review = review;
  }
}