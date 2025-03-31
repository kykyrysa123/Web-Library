package com.example.weblibrary.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

  private User user;
  private LocalDate registrationDate;
  private LocalDate lastLogin;

  @BeforeEach
  void setUp() {
    registrationDate = LocalDate.of(2023, 1, 1);
    lastLogin = LocalDate.of(2025, 3, 31);
    user = new User();
  }

  @Test
  void testDefaultConstructor() {
    assertNotNull(user);
    assertNull(user.getId());
    assertNull(user.getName());
    assertNull(user.getSurname());
    assertNull(user.getPatronymic());
    assertNull(user.getAge());
    assertNull(user.getSubscription());
    assertNull(user.getSex());
    assertNull(user.getCountry());
    assertNull(user.getEmail());
    assertNull(user.getPasswordHash());
    assertEquals(LocalDate.now(), user.getRegistrationDate());
    assertEquals(LocalDate.now(), user.getLastLogin());
    assertNotNull(user.getReviews());
    assertTrue(user.getReviews().isEmpty());
    assertNotNull(user.getFavouriteBooks());
    assertTrue(user.getFavouriteBooks().isEmpty());
  }

  @Test
  void testParameterizedConstructor() {
    User parameterizedUser = new User(
        1L, "John", "Doe", "Smith", 30, "Premium",
        "Male", "USA", "john.doe@example.com", "hashedPassword",
        registrationDate, lastLogin
    );

    assertEquals(1L, parameterizedUser.getId());
    assertEquals("John", parameterizedUser.getName());
    assertEquals("Doe", parameterizedUser.getSurname());
    assertEquals("Smith", parameterizedUser.getPatronymic());
    assertEquals(30, parameterizedUser.getAge());
    assertEquals("Premium", parameterizedUser.getSubscription());
    assertEquals("Male", parameterizedUser.getSex());
    assertEquals("USA", parameterizedUser.getCountry());
    assertEquals("john.doe@example.com", parameterizedUser.getEmail());
    assertEquals("hashedPassword", parameterizedUser.getPasswordHash());
    assertEquals(registrationDate, parameterizedUser.getRegistrationDate());
    assertEquals(lastLogin, parameterizedUser.getLastLogin());
    assertNotNull(parameterizedUser.getReviews());
    assertTrue(parameterizedUser.getReviews().isEmpty());
    assertNotNull(parameterizedUser.getFavouriteBooks());
    assertTrue(parameterizedUser.getFavouriteBooks().isEmpty());
  }

  @Test
  void testSettersAndGetters() {
    user.setId(2L);
    user.setName("Jane");
    user.setSurname("Doe");
    user.setPatronymic("Ann");
    user.setAge(25);
    user.setSubscription("Basic");
    user.setSex("Female");
    user.setCountry("Canada");
    user.setEmail("jane.doe@example.com");
    user.setPasswordHash("hashedPass123");
    user.setRegistrationDate(registrationDate);
    user.setLastLogin(lastLogin);

    assertEquals(2L, user.getId());
    assertEquals("Jane", user.getName());
    assertEquals("Doe", user.getSurname());
    assertEquals("Ann", user.getPatronymic());
    assertEquals(25, user.getAge());
    assertEquals("Basic", user.getSubscription());
    assertEquals("Female", user.getSex());
    assertEquals("Canada", user.getCountry());
    assertEquals("jane.doe@example.com", user.getEmail());
    assertEquals("hashedPass123", user.getPasswordHash());
    assertEquals(registrationDate, user.getRegistrationDate());
    assertEquals(lastLogin, user.getLastLogin());
  }

  @Test
  void testReviewsList() {
    Review review1 = new Review(); // Предполагается, что класс Review существует
    Review review2 = new Review();
    List<Review> reviews = new ArrayList<>();
    reviews.add(review1);
    reviews.add(review2);

    user.setReviews(reviews);
    assertEquals(2, user.getReviews().size());
    assertTrue(user.getReviews().contains(review1));
    assertTrue(user.getReviews().contains(review2));

    // Добавление через getter
    user.getReviews().add(new Review());
    assertEquals(3, user.getReviews().size());
  }

  @Test
  void testFavouriteBooksList() {
    Book book1 = new Book(); // Предполагается, что класс Book существует
    Book book2 = new Book();
    List<Book> favouriteBooks = new ArrayList<>();
    favouriteBooks.add(book1);
    favouriteBooks.add(book2);

    user.setFavouriteBooks(favouriteBooks);
    assertEquals(2, user.getFavouriteBooks().size());
    assertTrue(user.getFavouriteBooks().contains(book1));
    assertTrue(user.getFavouriteBooks().contains(book2));

    // Добавление через getter
    user.getFavouriteBooks().add(new Book());
    assertEquals(3, user.getFavouriteBooks().size());
  }
}