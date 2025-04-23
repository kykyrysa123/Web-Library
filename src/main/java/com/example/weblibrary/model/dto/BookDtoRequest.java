package com.example.weblibrary.model.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.validator.constraints.URL;

/**
 * Represents a request data transfer object (DTO) for creating or updating a book.
 */
public record BookDtoRequest(
    @NotBlank(message = "Название не должно быть пустым")
    @Size(max = 200, message = "Название не должно превышать 200 символов")
    String title,

    @NotBlank(message = "Издатель не должен быть пустым")
    @Size(max = 100, message = "Издатель не должен превышать 100 символов")
    String publisher,

    @Size(max = 13, message = "ISBN не должен превышать 13 символов")
    String isbn,

    @PositiveOrZero(message = "Количество страниц не должно быть отрицательным")
    @Max(value = 10000, message = "Количество страниц не должно превышать 10,000")
    Integer pages,

    @NotBlank(message = "Жанр не должен быть пустым")
    @Size(max = 50, message = "Жанр не должен превышать 50 символов")
    String genre,

    @PastOrPresent(message = "Дата публикации не может быть в будущем")
    LocalDate publishDate,

    @NotBlank(message = "Язык не должен быть пустым")
    @Size(min = 2, max = 30, message = "Язык должен быть от 2 до 30 символов")
    String language,

    @Size(max = 2000, message = "Описание не должно превышать 2000 символов")
    String description,

    @URL(message = "URL изображения должен быть действительным")
    String imageUrl,

    @DecimalMin(value = "0.0", message = "Рейтинг не может быть меньше 0")
    @DecimalMax(value = "5.0", message = "Рейтинг не может быть больше 5")
    Double rating,

    @URL(message = "URL для чтения должен быть действительным")
    String readUrl,

    @NotEmpty(message = "Необходимо указать хотя бы одного автора")
    List<@Positive(message = "ID автора должен быть положительным") Long> authorIds
) {
}