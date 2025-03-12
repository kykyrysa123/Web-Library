package com.example.weblibrary.mapper;

import com.example.weblibrary.model.Review;
import com.example.weblibrary.model.dto.ReviewDtoRequest;
import com.example.weblibrary.model.dto.ReviewDtoResponse;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between Review entity and DTOs.
 * Uses MapStruct for automatic mapping.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public interface ReviewMapper {

  /**
   * Converts a ReviewDtoRequest to a Review entity.
   *
   * @param reviewDtoRequest The DTO containing review data.
   * @return The corresponding Review entity.
   */
  Review toReviewEntity(ReviewDtoRequest reviewDtoRequest);

  /**
   * Converts a Review entity to a ReviewDtoResponse.
   *
   * @param review The Review entity.
   * @return The corresponding ReviewDtoResponse.
   */
  ReviewDtoResponse toReviewDtoResponse(Review review);

  /**
   * Converts a list of Review entities to a list of ReviewDtoResponse.
   *
   * @param review The list of Review entities.
   * @return The list of corresponding ReviewDtoResponse objects.
   */
  List<ReviewDtoResponse> toReviewDtoResponse(List<Review> review);
}
