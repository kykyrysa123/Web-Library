package com.example.weblibrary.repository;

import com.example.weblibrary.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing {@link Log.Review} entities.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Log.Review, Long> {
}
