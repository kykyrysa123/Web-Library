package com.example.weblibrary.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;
  private String surname;
  private String patronymic;
  private Integer age;
  private String subscription;
  private String sex;
  private String country;
  private String favoriteBooks;
  private String email;
  private String passwordHash;
  private LocalDate registrationDate;
  private LocalDate lastLogin;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> reviews = new ArrayList<>();

  @ManyToMany
  @JoinTable(name = "favourites", joinColumns = @JoinColumn(name = "user_id")
      , inverseJoinColumns = @JoinColumn(name = "book_id"))
  private List<Book> favouriteBooks = new ArrayList<>();
}
