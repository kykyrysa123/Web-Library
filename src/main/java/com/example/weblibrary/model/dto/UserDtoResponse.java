package com.example.weblibrary.model.dto;

import com.example.weblibrary.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoResponse {
  private Long id;
  private String username;
  private String email;
  private Role role;
  private LocalDate registrationDate;
}