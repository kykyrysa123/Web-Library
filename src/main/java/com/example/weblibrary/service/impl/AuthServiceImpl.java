package com.example.weblibrary.service.impl;

import com.example.weblibrary.mapper.AuthMapper;
import com.example.weblibrary.model.RefreshToken;
import com.example.weblibrary.model.User;
import com.example.weblibrary.model.dto.AuthResponse;
import com.example.weblibrary.model.enums.Role;
import com.example.weblibrary.repository.RefreshTokenRepository;
import com.example.weblibrary.repository.UserRepository;
import com.example.weblibrary.service.AuthService;
import com.example.weblibrary.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthMapper authMapper;
  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  @Value("${jwt.refresh-expiration}")
  private Long refreshExpiration;

  @Override
  @Transactional
  public AuthResponse login(String username, String password) {
    log.info("Login attempt: {}", username);

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
    );

    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    String token = jwtService.generateToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    saveRefreshToken(refreshToken, user);

    return authMapper.toAuthResponse(token, refreshToken, user);
  }

  @Override
  @Transactional
  public AuthResponse register(String username, String password, String email) {
    log.info("Register attempt: {}", username);

    if (userRepository.existsByUsername(username)) {
      throw new RuntimeException("Username already exists");
    }

    if (userRepository.existsByEmail(email)) {
      throw new RuntimeException("Email already exists");
    }

    User user = new User(username, passwordEncoder.encode(password), Role.ROLE_USER, email);
    userRepository.save(user);

    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    String token = jwtService.generateToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    saveRefreshToken(refreshToken, user);

    return authMapper.toAuthResponse(token, refreshToken, user);
  }

  @Override
  @Transactional
  public AuthResponse refresh(String auth) {
    log.info("Refresh token attempt");

    if (auth == null || !auth.startsWith("Bearer ")) {
      throw new RuntimeException("Refresh token is required");
    }

    String refreshTokenString = auth.substring(7);

    RefreshToken storedToken = refreshTokenRepository.findByToken(refreshTokenString)
            .orElseThrow(() -> new RuntimeException("Refresh token not found"));

    if (storedToken.isRevoked() || storedToken.getExpiryDate().isBefore(Instant.now())) {
      refreshTokenRepository.delete(storedToken);
      throw new RuntimeException("Refresh token is invalid or expired");
    }

    String username = jwtService.extractUsername(refreshTokenString);
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    if (!jwtService.validateToken(refreshTokenString, userDetails)) {
      throw new RuntimeException("Invalid refresh token");
    }

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    String newToken = jwtService.generateToken(userDetails);
    String newRefreshToken = jwtService.generateRefreshToken(userDetails);

    storedToken.changeRevoked(true);
    refreshTokenRepository.save(storedToken);

    saveRefreshToken(newRefreshToken, user);

    log.info("Tokens refreshed for user: {}", username);
    return authMapper.toAuthResponse(newToken, newRefreshToken, user);
  }

  @Override
  @Transactional
  public void logout(String auth) {
    if (auth != null && auth.startsWith("Bearer ")) {
      String token = auth.substring(7);
      refreshTokenRepository.findByToken(token).ifPresent(refreshToken -> {
        refreshToken.changeRevoked(true);
        refreshTokenRepository.save(refreshToken);
        log.info("Token revoked for user: {}", refreshToken.getUser().getUsername());
      });
    }
  }

  private void saveRefreshToken(String token, User user) {
    RefreshToken refreshToken = new RefreshToken(token, user, Instant.now().plusMillis(refreshExpiration));
    refreshTokenRepository.save(refreshToken);
  }
}