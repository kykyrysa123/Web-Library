package com.example.weblibrary.service.impl;

import com.example.weblibrary.mapper.ReviewMapperImpl;
import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.Review;
import com.example.weblibrary.model.User;
import com.example.weblibrary.model.dto.ReviewDtoRequest;
import com.example.weblibrary.model.dto.ReviewDtoResponse;
import com.example.weblibrary.repository.BookRepository;
import com.example.weblibrary.repository.ReviewRepository;
import com.example.weblibrary.repository.UserRepository;
import com.example.weblibrary.service.CrudService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of CRUD service for working with reviews.
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements CrudService<ReviewDtoRequest, ReviewDtoResponse> {

  private final ReviewRepository reviewRepository;
  private final ReviewMapperImpl reviewMapper;
  private final BookRepository bookRepository;
  private final UserRepository userRepository;

  @Override
  public List<ReviewDtoResponse> getAll() {
    return reviewMapper.toReviewDtoResponse(reviewRepository.findAll());
  }

  @Override
  public ReviewDtoResponse getById(Long id) {
    return reviewMapper.toReviewDtoResponse(
        reviewRepository.findById(id)
                        .orElseThrow(NullPointerException::new)
    );
  }

  @Override
  public ReviewDtoResponse create(ReviewDtoRequest reviewDtoRequest) {
    Book book = bookRepository.findById(reviewDtoRequest.bookId())
                              .orElseThrow(NullPointerException::new);
    User user = userRepository.findById(reviewDtoRequest.userId())
                              .orElseThrow(NullPointerException::new);

    Review review = reviewMapper.toReviewEntity(reviewDtoRequest);
    review.setBook(book);
    review.setUser(user);

    return reviewMapper.toReviewDtoResponse(reviewRepository.save(review));
  }

  @Override
  public ReviewDtoResponse update(Long id, ReviewDtoRequest reviewDtoRequest) {
    Review savedReview = reviewRepository.findById(id).orElseThrow(
        () -> new RuntimeException("Review not found with id: " + id));

    Review updateReview = reviewMapper.toReviewEntity(reviewDtoRequest);
    updateReview.setId(id);
    updateReview.setBook(savedReview.getBook());
    updateReview.setUser(savedReview.getUser());

    return reviewMapper.toReviewDtoResponse(reviewRepository.save(updateReview));
  }

  @Override
  public void delete(Long id) {
    Review review = reviewRepository.findById(id)
                                    .orElseThrow(() ->
                                        new RuntimeException("Review not found with id: " + id));
    reviewRepository.delete(review);
  }
}
