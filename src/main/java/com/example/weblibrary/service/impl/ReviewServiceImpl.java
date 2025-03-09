package com.example.weblibrary.service.impl;

import com.example.weblibrary.model.Review;
import com.example.weblibrary.repository.ReviewRepository;
import com.example.weblibrary.service.CrudService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;


/**
 * Service implementation for managing {@link Review} entities.
 */
@Service
public class ReviewServiceImpl implements CrudService<Review> {

  private final ReviewRepository reviewRepository;

  /**
   * Constructor for dependency injection.
   *
   * @param reviewRepository The repository for {@link Review} entities.
   */
  public ReviewServiceImpl(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }

  /**
   * Retrieve all reviews.
   *
   * @return A list of all {@link Review} entities.
   */
  @Override
  public List<Review> getAll() {
    return reviewRepository.findAll();  // Возвращаем все отзывы
  }

  /**
   * Retrieve a review by its ID.
   *
   * @param id The ID of the review.
   * @return An {@link Optional} containing the review if found, empty otherwise.
   */
  @Override
  public Optional<Review> getById(int id) {
    return reviewRepository.findById(id);  // Ищем отзыв по id, приводим к Long
  }

  /**
   * Create a new review.
   *
   * @param review The review to create.
   * @return The created {@link Review} entity.
   */
  @Override
  public Review create(Review review) {
    return reviewRepository.save(review);  // Сохраняем новый отзыв
  }

  /**
   * Update an existing review.
   *
   * @param id The ID of the review to update.
   * @param review The review data to update.
   * @return The updated {@link Review} entity.
   */
  @Override
  public Review update(int id, Review review) {
    if (reviewRepository.existsById(id)) {  // Проверяем, существует ли отзыв с таким id
      review.setId(id);  // Приводим id к типу Long
      return reviewRepository.save(review);  // Обновляем отзыв
    } else {
      throw new RuntimeException("Review not found with id: " + id);  // Если отзыв не найден
    }
  }

  /**
   * Delete a review by its ID.
   *
   * @param id The ID of the review to delete.
   */
  @Override
  public void delete(int id) {
    if (reviewRepository.existsById(id)) {  // Проверяем, существует ли отзыв с таким id
      reviewRepository.deleteById(id);  // Удаляем отзыв
    } else {
      throw new RuntimeException("Review not found with id: " + id);  // Если отзыв не найден
    }
  }
}
