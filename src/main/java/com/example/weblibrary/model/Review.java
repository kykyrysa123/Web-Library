package com.example.weblibrary.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents a review for a book, including the rating, review text, and user
 * information.
 */
@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @NonNull
  private Double rating;

  private String reviewText;

  private LocalDate reviewDate;
}
