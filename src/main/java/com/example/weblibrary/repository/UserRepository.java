package com.example.weblibrary.repository;

import com.example.weblibrary.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods to perform CRUD operations on users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Finds a user by ID along with their reviews and favourite books
   * (uses JOIN FETCH to avoid N+1 problem).
   *
   * @param id the ID of the user
   * @return the user with their reviews and favourite books
   */
  @Query("SELECT u FROM User u LEFT JOIN FETCH u.reviews"
      + " LEFT JOIN FETCH u.favouriteBooks WHERE u.id = :id")
  Optional<User> findByIdWithReviewsAndFavouriteBooks(@Param("id") Long id);

  /**
   * Finds all users along with their reviews and favourite books
   * (uses JOIN FETCH to avoid N+1 problem).
   *
   * @return a list of users with their reviews and favourite books
   */
  @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.reviews"
      + " LEFT JOIN FETCH u.favouriteBooks")
  List<User> findAllWithReviewsAndFavouriteBooks();

  /**
   * Finds users by their first name (case insensitive).
   *
   * @param name the user's first name
   * @return a list of users with the specified first name
   */
  List<User> findByNameContainingIgnoreCase(String name);

  /**
   * Finds users by their last name (case insensitive).
   *
   * @param surname the user's last name
   * @return a list of users with the specified last name
   */
  List<User> findBySurnameContainingIgnoreCase(String surname);

  /**
   * Finds a user by their email (case insensitive).
   *
   * @param email the user's email
   * @return the user with the specified email
   */
  Optional<User> findByEmailIgnoreCase(String email);

  /**
   * Finds users by their country.
   *
   * @param country the country of the user
   * @return a list of users from the specified country
   */
  List<User> findByCountry(String country);

  /**
   * Finds users by their country with pagination.
   *
   * @param country  the country of the user
   * @param pageable pagination details
   * @return a page of users from the specified country
   */
  Page<User> findByCountry(String country, Pageable pageable);

  /**
   * Finds users who have reviews with a rating greater than or equal to the specified value.
   *
   * @param rating the minimum rating
   * @return a list of users with reviews >= the specified rating
   */
  @Query("SELECT DISTINCT u FROM User u JOIN u.reviews r WHERE r.rating >= :rating")
  List<User> findUsersWithReviewsRatingGreaterThanEqual(@Param("rating") Double rating);

  /**
   * Finds users who have reviews with a rating less than or equal to the specified value.
   *
   * @param rating the maximum rating
   * @return a list of users with reviews <= the specified rating
   */
  @Query("SELECT DISTINCT u FROM User u JOIN u.reviews r WHERE r.rating <= :rating")
  List<User> findUsersWithReviewsRatingLessThanEqual(@Param("rating") Double rating);
}
