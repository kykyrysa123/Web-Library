package com.example.weblibrary.model;

import com.example.weblibrary.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Table(name = "users")
@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  private String email;

  @Enumerated(EnumType.STRING)
  private Role role;

  private boolean enabled = true;
  private LocalDate registrationDate = LocalDate.now();

  public User(String username, String password, Role role, String email) {
    this.username = username;
    this.password = password;
    this.role = role;
    this.email = email;
  }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public boolean isAccountNonExpired() { return true; }

  @Override
  public boolean isAccountNonLocked() { return true; }

  @Override
  public boolean isCredentialsNonExpired() { return true; }

  @Override
  public boolean isEnabled() { return enabled; }

}
