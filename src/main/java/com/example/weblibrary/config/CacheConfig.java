package com.example.weblibrary.config;

import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.service.cache.SimpleCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CacheConfig {

  @Bean
  public SimpleCache<Long, BookDtoResponse> bookCache() {
    return new SimpleCache<>(100); // Размер кэша можно настроить
  }

  @Bean
  public SimpleCache<String, List<BookDtoResponse>> bookListCache() {
    return new SimpleCache<>(100); // Размер кэша можно настроить
  }
}