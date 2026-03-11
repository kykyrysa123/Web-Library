package com.example.weblibrary.security;

import com.example.weblibrary.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    // Публичные endpoints
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                    // Авторы - все могут просматривать, только ADMIN изменять
                    .requestMatchers(HttpMethod.GET, "/api/authors/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/authors/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/authors/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/authors/**").hasRole("ADMIN")

                    // Книги - все могут просматривать, только ADMIN изменять
                    .requestMatchers(HttpMethod.GET, "/api/books/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/books/bulk").hasRole("ADMIN")

                    // Отзывы - специальные правила
                    .requestMatchers(HttpMethod.GET, "/api/reviews/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/reviews").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/reviews/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/reviews/**").hasRole("ADMIN")

                    // Пользователи - только ADMIN
                    .requestMatchers("/api/users/**").hasRole("ADMIN")

                    // Посещения - все аутентифицированные
                    .requestMatchers("/api/visits/**").hasAnyRole("USER", "ADMIN")

                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtService, userDetailsService);
  }

  @Bean
  public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
          JwtAuthenticationFilter filter
  ) {
    FilterRegistrationBean<JwtAuthenticationFilter> registration =
            new FilterRegistrationBean<>(filter);
    registration.setEnabled(false); // Отключаем автоматическую регистрацию фильтра
    return registration;
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}