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
 * Represents a user in the system.
 * This class contains information about a user such as name, subscription, and reviews.
 */
@Entity
@Table(name = "users")
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

  /**
   * Default constructor required by JPA.
   */
  public User() {
  }

  /**
   * Constructs a User with all required fields.
   *
   * @param id the unique identifier for the user
   * @param name the user's first name
   * @param surname the user's last name
   * @param patronymic the user's middle name
   * @param age the user's age
   * @param subscription the user's subscription type
   * @param sex the user's gender
   * @param country the user's country
   * @param email the user's email address
   * @param passwordHash the hashed user password
   * @param registrationDate the date when user registered
   * @param lastLogin the date of user's last login
   */
  public User(Long id, String name, String surname, String patronymic,
      Integer age, String subscription, String sex, String country,
      String email, String passwordHash, LocalDate registrationDate,
      LocalDate lastLogin) {
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

  // Getters and setters below...
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getPatronymic() {
    return patronymic;
  }

  public void setPatronymic(String patronymic) {
    this.patronymic = patronymic;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getSubscription() {
    return subscription;
  }

  public void setSubscription(String subscription) {
    this.subscription = subscription;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public LocalDate getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(LocalDate registrationDate) {
    this.registrationDate = registrationDate;
  }

  public LocalDate getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(LocalDate lastLogin) {
    this.lastLogin = lastLogin;
  }

  public List<Review> getReviews() {
    return reviews;
  }

  public void setReviews(List<Review> reviews) {
    this.reviews = reviews;
  }

  public List<Book> getFavouriteBooks() {
    return favouriteBooks;
  }

  public void setFavouriteBooks(List<Book> favouriteBooks) {
    this.favouriteBooks = favouriteBooks;
  }
}