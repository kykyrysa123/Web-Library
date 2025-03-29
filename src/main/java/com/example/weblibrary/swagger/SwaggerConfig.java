package com.example.weblibrary.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger/OpenAPI documentation.
 * Defines the API metadata and documentation settings.
 */
@Configuration
public class SwaggerConfig {

  /**
   * Creates and configures the OpenAPI documentation.
   *
   * @return configured OpenAPI instance with API metadata
   */
  @Bean
  public OpenAPI libraryOpenApi() {
    return new OpenAPI()
        .info(new Info()
            .title("Web Library API")
            .version("1.0")
            .description("Документация API для управления библиотекой"));
  }
}