package com.example.weblibrary.service.cache;

import com.example.weblibrary.model.dto.AuthorDtoResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CacheConfig {

  @Bean
  public SimpleCache<Long, AuthorDtoResponse> authorCache() {
    return new SimpleCache<>(100); // 100 - пример максимального размера кэша
  }

  @Bean
  public SimpleCache<String, List<AuthorDtoResponse>> authorListCache() {
    return new SimpleCache<>(50); // 50 - пример максимального размера кэша
  }
}
