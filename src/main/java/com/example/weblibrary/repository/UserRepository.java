package com.example.weblibrary.repository;

import com.example.weblibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods to perform CRUD operations on users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
