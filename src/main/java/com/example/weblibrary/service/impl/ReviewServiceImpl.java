package com.example.weblibrary.service.impl;

import com.example.weblibrary.mapper.ReviewMapperImpl;
import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.Log;
import com.example.weblibrary.model.User;
import com.example.weblibrary.model.dto.ReviewDtoRequest;
import com.example.weblibrary.model.dto.ReviewDtoResponse;
import com.example.weblibrary.repository.BookRepository;
import com.example.weblibrary.repository.ReviewRepository;
import com.example.weblibrary.repository.UserRepository;
import com.example.weblibrary.service.CrudService;
import com.example.weblibrary.service.cache.SimpleCache;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of CRUD service for working with reviews.
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements CrudService<ReviewDtoRequest,
    ReviewDtoResponse> {
  private static final String ALL_REVIEWS_CACHE_KEY = "all_reviews";
  private static final String REVIEW_NOT_FOUND_MESSAGE = "Review not found with id: ";

  private final ReviewRepository reviewRepository;
  private final ReviewMapperImpl reviewMapper;
  private final BookRepository bookRepository;
  private final UserRepository userRepository;

  private final SimpleCache<Long, ReviewDtoResponse> reviewCache =
      new SimpleCache<>(
      100);
  private final SimpleCache<String, List<ReviewDtoResponse>> reviewListCache
      = new SimpleCache<>(
      100);
  private static final Logger log = LoggerFactory.getLogger(
      ReviewServiceImpl.class);

  @Override
  public List<ReviewDtoResponse> getAll() {
    String cacheKey = ALL_REVIEWS_CACHE_KEY;

    List<ReviewDtoResponse> cachedList = reviewListCache.get(cacheKey);
    if (cachedList != null) {
      log.debug("Data on all reviews is obtained from the cache");
      return cachedList;
    }

    log.debug("Data on all reviews is downloaded from the database");
    List<ReviewDtoResponse> responseList = reviewMapper.toReviewDtoResponse(
        reviewRepository.findAll());
    reviewListCache.put(cacheKey, responseList);
    return responseList;
  }

  @Override
  public ReviewDtoResponse getById(Long id) {
    ReviewDtoResponse cachedReview = reviewCache.get(id);
    if (cachedReview != null) {
      log.debug("Review with id={} retrieved from cache", id);
      return cachedReview;
    }

    log.debug("Review with id={} retrieved from the database", id);
    Log.Review review = reviewRepository.findById(id).orElseThrow(
        () -> new RuntimeException(REVIEW_NOT_FOUND_MESSAGE + id));

    ReviewDtoResponse response = reviewMapper.toReviewDtoResponse(review);
    reviewCache.put(id, response);
    return response;
  }

  @Override
  public ReviewDtoResponse create(ReviewDtoRequest reviewDtoRequest) {
    Book book = bookRepository.findById(reviewDtoRequest.bookId()).orElseThrow(
        () -> new RuntimeException(
            "Book not found with id: " + reviewDtoRequest.bookId()));

    User user = userRepository.findById(reviewDtoRequest.userId()).orElseThrow(
        () -> new RuntimeException(
            "User not found with id: " + reviewDtoRequest.userId()));

    Log.Review review = reviewMapper.toReviewEntity(reviewDtoRequest);
    review.setBook(book);
    review.setUser(user);

    Log.Review savedReview = reviewRepository.save(review);
    ReviewDtoResponse response = reviewMapper.toReviewDtoResponse(savedReview);

    reviewCache.put(savedReview.getId(), response);
    reviewListCache.remove(ALL_REVIEWS_CACHE_KEY); // Удаляем только кэш списка

    return response;
  }

  @Override
  public ReviewDtoResponse update(Long id, ReviewDtoRequest reviewDtoRequest) {
    Log.Review savedReview = reviewRepository.findById(id).orElseThrow(
        () -> new RuntimeException(REVIEW_NOT_FOUND_MESSAGE + id));

    Log.Review updateReview = reviewMapper.toReviewEntity(reviewDtoRequest);
    updateReview.setId(id);
    updateReview.setBook(savedReview.getBook());
    updateReview.setUser(savedReview.getUser());

    Log.Review updatedReview = reviewRepository.save(updateReview);
    ReviewDtoResponse response = reviewMapper.toReviewDtoResponse(
        updatedReview);

    reviewCache.put(id, response);
    reviewListCache.clear(); // Очистка кэша списка отзывов

    return response;
  }

  @Override
  public void delete(Long id) {
    Log.Review review = reviewRepository.findById(id).orElseThrow(
        () -> new RuntimeException(REVIEW_NOT_FOUND_MESSAGE + id));

    reviewRepository.delete(review);
    reviewCache.remove(id);
    reviewListCache.remove(ALL_REVIEWS_CACHE_KEY); // Удаляем только кэш списка
  }

}
