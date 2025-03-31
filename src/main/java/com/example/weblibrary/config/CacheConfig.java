package com.example.weblibrary.config;

import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.service.cache.SimpleCache;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up application caches.
 * Defines beans for various cache implementations used in the application.
 */
@Configuration
public class CacheConfig {

  /**
   * Creates a cache for individual book responses.
   *
   * @return SimpleCache instance configured to store BookDtoResponse objects
   *         with a maximum size of 100 entries
   */
  @Bean
  public SimpleCache<Long, BookDtoResponse> bookCache() {
    return new SimpleCache<>(100);
  }

  /**
   * Creates a cache for lists of book responses.
   *
   * @return SimpleCache instance configured to store lists of BookDtoResponse objects
   *         with a maximum size of 100 entries
   */
  @Bean
  public SimpleCache<String, List<BookDtoResponse>> bookListCache() {
    return new SimpleCache<>(100);
  }
}