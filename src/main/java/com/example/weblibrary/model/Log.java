package com.example.weblibrary.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.NonNull;

/**
 * Entity class representing a Log entry.
 */
@Entity
public class Log {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String status;
  private String filePath;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /**
   * Represents a review for a book, including the rating, review text, and user information.
   */
  @Entity
  @Table(name = "reviews")
  public static class Review {

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
     * Constructs a Review with all required fields.
     *
     * @param id the unique identifier for the review
     * @param book the book being reviewed
     * @param user the user who wrote the review
     * @param rating the rating given (must not be null)
     * @param reviewText the text content of the review
     * @param reviewDate the date when the review was written
     */
    public Review(Long id, Book book, User user, @NonNull Double rating,
        String reviewText, LocalDate reviewDate) {
      this.id = id;
      this.book = book;
      this.user = user;
      this.rating = rating;
      this.reviewText = reviewText;
      this.reviewDate = reviewDate;
    }

    /**
     * Default constructor required by JPA.
     */
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
}