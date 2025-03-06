package com.example.weblibrary.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
  private Author author;
}