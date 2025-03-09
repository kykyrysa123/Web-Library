package com.example.weblibrary.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
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
@NoArgsConstructor
@RequiredArgsConstructor
@Setter  // Автоматическая генерация всех сеттеров
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  private String title;

  @NonNull
  private String publisher;

  private String isbn;

  private Integer pages;

  @NonNull
  private String genre;

  private LocalDate publishDate;

  @NonNull
  private String language;

  private String description;

  private String imageUrl;

  private Double rating;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  @JsonBackReference
  private Author author;
}
