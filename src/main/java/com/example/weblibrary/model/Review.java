package com.example.weblibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

import lombok.*;

/**
 * Represents a review for a book, including the rating, review text, and user
 * information.
 */
@Entity
@Table(name = "reviews")
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user", nullable = false)
  private User user;

  @NonNull
  private Double rating;

  private String reviewText;

  private LocalDate reviewDate;

  /**
   * Constructs a Review with the specified ID and review text.
   *
   * @param id the unique identifier for the review
   * @param reviewText the text content of the review
   */
  public Review(Long id, Book book, User user, @NonNull Double rating,
      String reviewText, LocalDate reviewDate
  ) {
    this.id = id;
    this.book = book;
    this.user = user;
    this.rating = rating;
    this.reviewText = reviewText;
    this.reviewDate = reviewDate;
  }

  public Review() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public @NonNull Double getRating() {
    return rating;
  }

  public void setRating(@NonNull Double rating) {
    this.rating = rating;
  }

  public String getReviewText() {
    return reviewText;
  }

  public void setReviewText(String reviewText) {
    this.reviewText = reviewText;
  }

  public LocalDate getReviewDate() {
    return reviewDate;
  }

  public void setReviewDate(LocalDate reviewDate) {
    this.reviewDate = reviewDate;
  }
}