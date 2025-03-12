package com.example.weblibrary.repository;

import com.example.weblibrary.model.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing {@link Review} entities.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

  /**
   * Finds a review by ID along with its associated book and user (uses JOIN FETCH to avoid N+1 problem).
   *
   * @param id the ID of the review
   * @return the review with its associated book and user
   */
  @Query("SELECT r FROM Review r "
      + "JOIN FETCH r.book "
      + "JOIN FETCH r.user "
      + "WHERE r.id = :id")
  Optional<Review> findByIdWithBookAndUser(@Param("id") Long id);

  /**
   * Finds all reviews along with their associated books and users
   * (uses JOIN FETCH to avoid N+1 problem).
   *
   * @return a list of reviews with their associated books and users
   */
  @Query("SELECT DISTINCT r FROM Review r "
      + "JOIN FETCH r.book "
      + "JOIN FETCH r.user")
  List<Review> findAllWithBooksAndUsers();

  /**
   * Finds reviews with a rating greater than or equal to the specified value.
   *
   * @param rating the minimum rating
   * @return a list of reviews with a rating greater than or equal to the specified value
   */
  List<Review> findByRatingGreaterThanEqual(Double rating);

  /**
   * Finds reviews with a rating less than or equal to the specified value.
   *
   * @param rating the maximum rating
   * @return a list of reviews with a rating less than or equal to the specified value
   */
  List<Review> findByRatingLessThanEqual(Double rating);

  /**
   * Finds reviews by book ID.
   *
   * @param bookId the ID of the book
   * @return a list of reviews for the specified book
   */
  List<Review> findByBookId(Long bookId);

  /**
   * Finds reviews by book ID with pagination.
   *
   * @param bookId the ID of the book
   * @param pageable pagination details
   * @return a page of reviews for the specified book
   */
  Page<Review> findByBookId(Long bookId, Pageable pageable);

  /**
   * Finds reviews by user ID.
   *
   * @param userId the ID of the user
   * @return a list of reviews for the specified user
   */
  List<Review> findByUserId(Long userId);

  /**
   * Finds reviews by user ID with pagination.
   *
   * @param userId the ID of the user
   * @param pageable pagination details
   * @return a page of reviews for the specified user
   */
  Page<Review> findByUserId(Long userId, Pageable pageable);
}
