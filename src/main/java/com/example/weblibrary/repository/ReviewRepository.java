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
}
