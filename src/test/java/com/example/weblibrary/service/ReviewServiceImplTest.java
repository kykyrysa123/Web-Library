/*
package com.example.weblibrary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.example.weblibrary.mapper.ReviewMapperImpl;
import com.example.weblibrary.model.Review;
import com.example.weblibrary.model.dto.ReviewDtoRequest;
import com.example.weblibrary.model.dto.ReviewDtoResponse;
import com.example.weblibrary.repository.ReviewRepository;
import com.example.weblibrary.service.impl.ReviewServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private ReviewMapperImpl reviewMapper;

  @InjectMocks
  private ReviewServiceImpl reviewService;

  private Review review;
  private ReviewDtoRequest reviewDtoRequest;
  private ReviewDtoResponse reviewDtoResponse;

  @BeforeEach
  void setUp() {
    review = new Review(1L, "Great book!");
    reviewDtoRequest = new ReviewDtoRequest("Great book!");
    reviewDtoResponse = new ReviewDtoResponse(1L, "Great book!");
  }

  @Test
  void getAllReviews_ShouldReturnReviewList() {
    when(reviewRepository.findAll()).thenReturn(List.of(review));
    when(reviewMapper.toReviewDtoResponse(List.of(review))).thenReturn(List.of(reviewDtoResponse));

    List<ReviewDtoResponse> reviews = reviewService.getAll();

    assertThat(reviews).hasSize(1);
    assertThat(reviews.get(0).id()).isEqualTo(1L);
    verify(reviewRepository).findAll();
  }

  @Test
  void getReviewById_ShouldReturnReview() {
    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
    when(reviewMapper.toReviewDtoResponse(review)).thenReturn(reviewDtoResponse);

    ReviewDtoResponse foundReview = reviewService.getById(1L);

    assertThat(foundReview.id()).isEqualTo(1L);
    verify(reviewRepository).findById(1L);
  }

  @Test
  void createReview_ShouldSaveReview() {
    when(reviewMapper.toReviewEntity(reviewDtoRequest)).thenReturn(review);
    when(reviewRepository.save(review)).thenReturn(review);
    when(reviewMapper.toReviewDtoResponse(review)).thenReturn(reviewDtoResponse);

    ReviewDtoResponse createdReview = reviewService.create(reviewDtoRequest);

    assertThat(createdReview.id()).isEqualTo(1L);
    verify(reviewRepository).save(review);
  }

  @Test
  void updateReview_ShouldUpdateReview() {
    when(reviewMapper.toReviewEntity(reviewDtoRequest)).thenReturn(review);
    when(reviewRepository.save(review)).thenReturn(review);
    when(reviewMapper.toReviewDtoResponse(review)).thenReturn(reviewDtoResponse);

    ReviewDtoResponse updatedReview = reviewService.update(1L, reviewDtoRequest);

    assertThat(updatedReview.id()).isEqualTo(1L);
    verify(reviewRepository).save(review);
  }

  @Test
  void deleteReview_ShouldRemoveReview() {
    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
    doNothing().when(reviewRepository).delete(review);

    reviewService.delete(1L);

    verify(reviewRepository).delete(review);
  }
}*/
