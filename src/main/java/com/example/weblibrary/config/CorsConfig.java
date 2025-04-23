/*
package com.example.weblibrary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

*/
/**
 * Configuration class for Cross-Origin Resource Sharing (CORS) settings.
 * Defines allowed origins, methods, headers, and other CORS-related configurations
 * for the application.
 *//*

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000", "http://localhost:80", "https://web-library-production.up.railway.app/")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
  }
}*/
