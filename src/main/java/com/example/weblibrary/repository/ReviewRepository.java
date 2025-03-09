package com.example.weblibrary.repository;

import com.example.weblibrary.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing {@link Review} entities.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
