package com.example.weblibrary.service.impl;

import com.example.weblibrary.model.Review;
import com.example.weblibrary.repository.ReviewRepository;
import com.example.weblibrary.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements CrudService<Review> {

  private final ReviewRepository reviewRepository;

  // Конструктор для внедрения зависимостей
  public ReviewServiceImpl(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }

  @Override
  public List<Review> getAll() {
    return reviewRepository.findAll();  // Возвращаем все отзывы
  }

  @Override
  public Optional<Review> getById(int id) {
    return reviewRepository.findById(id);  // Ищем отзыв по id, приводим к Long
  }

  @Override
  public Review create(Review review) {
    return reviewRepository.save(review);  // Сохраняем новый отзыв
  }

  @Override
  public Review update(int id, Review review) {
    if (reviewRepository.existsById(
        id)) {  // Проверяем, существует ли отзыв с таким id
      review.setId((id));  // Приводим id к типу Long
      return reviewRepository.save(review);  // Обновляем отзыв
    } else {
      throw new RuntimeException(
          "Review not found with id: " + id);  // Если отзыв не найден
    }
  }

  @Override
  public void delete(int id) {
    if (reviewRepository.existsById(
        id)) {  // Проверяем, существует ли отзыв с таким id
      reviewRepository.deleteById(id);  // Удаляем отзыв
    } else {
      throw new RuntimeException(
          "Review not found with id: " + id);  // Если отзыв не найден
    }
  }
}
