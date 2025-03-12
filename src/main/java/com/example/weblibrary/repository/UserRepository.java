package com.example.weblibrary.repository;

import com.example.weblibrary.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods to perform CRUD operations on users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
