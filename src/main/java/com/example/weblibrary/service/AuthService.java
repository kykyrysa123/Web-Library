package com.example.weblibrary.service;

import com.example.weblibrary.model.dto.AuthResponse;

public interface AuthService {
  AuthResponse login(String username, String password);
  AuthResponse register(String username, String password,String email);
  AuthResponse refresh(String auth);
  void logout(String auth);
}
