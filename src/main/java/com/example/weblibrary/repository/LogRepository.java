package com.example.weblibrary.repository;

import com.example.weblibrary.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link Log} entity.
 * Provides CRUD operations and query methods for log entries.
 */
public interface LogRepository extends JpaRepository<Log, Long> {
}