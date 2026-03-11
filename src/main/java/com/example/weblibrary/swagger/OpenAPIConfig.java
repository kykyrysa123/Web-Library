package com.example.weblibrary.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(new Info()
                    .title("Web Library API")
                    .version("1.0")
                    .description("API для управления библиотекой")
                    .contact(new Contact()
                            .name("Your Name")
                            .email("your.email@example.com")
                            .url("https://example.com"))
                    .license(new License()
                            .name("Apache 2.0")
                            .url("http://springdoc.org")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
  }
}