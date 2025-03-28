package com.example.weblibrary.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user in the systemm.
 * This class contains information about a user such as name, subscription, and reviews.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String surname;
  private String patronymic;
  private Integer age;
  private String subscription;
  private String sex;
  private String country;
  private String email;
  private String passwordHash;
  private LocalDate registrationDate = LocalDate.now();
  private LocalDate lastLogin = LocalDate.now();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
      orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Review> reviews = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "favourites",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "book_id")
  )
  private List<Book> favouriteBooks = new ArrayList<>();

  public User(Long id, String name, String surname, String patronymic,
      Integer age, String subscription, String sex, String country,
      String email, String passwordHash, LocalDate registrationDate,
      LocalDate lastLogin
  ) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.patronymic = patronymic;
    this.age = age;
    this.subscription = subscription;
    this.sex = sex;
    this.country = country;
    this.email = email;
    this.passwordHash = passwordHash;
    this.registrationDate = registrationDate;
    this.lastLogin = lastLogin;

  }
}
