package com.example.weblibrary.model.dto;

import com.example.weblibrary.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object for creating a user. Contains validation constraints for
 * user registration and updates.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDtoRequest{
        @Null(message = "ID should not be provided for new users")
        Long id;

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores and hyphens")
        String username;

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email;

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "Password must contain at least one digit, one lowercase letter, "
                        + "one uppercase letter, and one special character (@#$%^&+=!)")
        String password;

        @Pattern(regexp = "^(ROLE_USER|ROLE_ADMIN|ROLE_LIBRARIAN)$",
                message = "Role must be ROLE_USER, ROLE_ADMIN or ROLE_LIBRARIAN")
        Role role;
}
