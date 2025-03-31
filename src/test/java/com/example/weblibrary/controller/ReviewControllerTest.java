package com.example.weblibrary.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.weblibrary.controllers.ReviewController;
import com.example.weblibrary.model.dto.ReviewDtoRequest;
import com.example.weblibrary.model.dto.ReviewDtoResponse;
import com.example.weblibrary.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

  @InjectMocks
  private ReviewController reviewController;

  @Mock
  private ReviewServiceImpl reviewService;

  private ReviewDtoRequest reviewRequest;
  private ReviewDtoResponse reviewResponse;

  @BeforeEach
  void setUp() {
    reviewRequest = new ReviewDtoRequest(null, 1L, 1L, 4.5, "Great book!", LocalDate.now());
    reviewResponse = new ReviewDtoResponse(1L, null, null, 4.5, "Great book!", LocalDate.now());
  }

  @Test
  void testGetAllReviews_Success() {
    List<ReviewDtoResponse> reviews = List.of(reviewResponse);
    when(reviewService.getAll()).thenReturn(reviews);

    ResponseEntity<List<ReviewDtoResponse>> response = reviewController.getAllReviews();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(reviews, response.getBody());
  }

  @Test
  void testGetReviewById_Success() {
    Long id = 1L;
    when(reviewService.getById(id)).thenReturn(reviewResponse);

    ResponseEntity<ReviewDtoResponse> response = reviewController.getReviewById(id);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(reviewResponse, response.getBody());
  }

  @Test
  void testCreateReview_Success() {
    when(reviewService.create(reviewRequest)).thenReturn(reviewResponse);

    ResponseEntity<ReviewDtoResponse> response = reviewController.createReview(reviewRequest);

    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(reviewResponse, response.getBody());
  }

  @Test
  void testUpdateReview_Success() {
    Long id = 1L;
    when(reviewService.update(id, reviewRequest)).thenReturn(reviewResponse);

    ResponseEntity<ReviewDtoResponse> response = reviewController.updateReview(id, reviewRequest);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(reviewResponse, response.getBody());
  }

  @Test
  void testDeleteReview_Success() {
    Long id = 1L;
    doNothing().when(reviewService).delete(id);

    ResponseEntity<Void> response = reviewController.deleteReview(id);

    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }
}