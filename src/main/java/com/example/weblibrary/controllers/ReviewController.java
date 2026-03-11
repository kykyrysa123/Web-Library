package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.ReviewDtoRequest;
import com.example.weblibrary.model.dto.ReviewDtoResponse;
import com.example.weblibrary.service.impl.ReviewServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Управление отзывами")
@SecurityRequirement(name = "Bearer Authentication")
public class ReviewController {

  private final ReviewServiceImpl reviewService;

  @GetMapping
  @Operation(summary = "Получить все отзывы")
  public ResponseEntity<List<ReviewDtoResponse>> getAllReviews() {
    return ResponseEntity.ok(reviewService.getAll());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить отзыв по ID")
  public ResponseEntity<ReviewDtoResponse> getReviewById(@PathVariable Long id) {
    return ResponseEntity.ok(reviewService.getById(id));
  }

  @PostMapping
  @Operation(summary = "Создать отзыв")
  public ResponseEntity<ReviewDtoResponse> createReview(@Valid @RequestBody ReviewDtoRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(request));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить отзыв (только владелец или ADMIN)")
  public ResponseEntity<ReviewDtoResponse> updateReview(@PathVariable Long id,
                                                        @Valid @RequestBody ReviewDtoRequest request) {
    return ResponseEntity.ok(reviewService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Удалить отзыв (только ADMIN)")
  public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
    reviewService.delete(id);
    return ResponseEntity.noContent().build();
  }
}