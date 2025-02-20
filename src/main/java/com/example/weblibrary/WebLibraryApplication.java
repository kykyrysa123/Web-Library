package com.example.weblibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This package contains the core components for a blog application built
 * with Spring Boot. It includes the main application class and potential
 * supporting classes for blog functionalities.
 */
@SpringBootApplication
public class WebLibraryApplication {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(final String[] args) {
        SpringApplication.run(WebLibraryApplication.class, args);
    }

}
