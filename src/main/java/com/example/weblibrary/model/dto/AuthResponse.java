package com.example.weblibrary.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
  private String token;
  private String refreshToken;
  private String username;

  public AuthResponse(String token, String refreshToken, String username) {
    this.token = token;
    this.refreshToken = refreshToken;
    this.username = username;
  }
  public AuthResponse() {
  }
}
