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
 * Represents a Book entity in the web library systemm.
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private Author author;

  @OneToMany(mappedBy = "book", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> review;
}
