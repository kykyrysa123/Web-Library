package com.example.weblibrary.repository;

import com.example.weblibrary.model.RefreshToken;
import com.example.weblibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByToken(String token);

  List<RefreshToken> findAllByUser(User user);

  @Modifying
  @Transactional
  @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
  void deleteAllByUser(@Param("user") User user);

  @Modifying
  @Transactional
  @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now OR rt.revoked = true")
  void deleteAllExpiredOrRevoked(@Param("now") Instant now);

  boolean existsByTokenAndRevokedFalse(String token);
}