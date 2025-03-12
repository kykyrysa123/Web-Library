package com.example.weblibrary.model.dto;

import java.time.LocalDate;

/**
 * Represents a request data transfer object (DTO) for creating or updating a book.
 * This record encapsulates the necessary details about a book, such as its title,
 * publisher, genre, and other relevant information.
 *
 * @param title       the title of the book.
 * @param publisher   the publisher of the book.
 * @param isbn        the ISBN of the book.
 * @param pages       the number of pages in the book.
 * @param genre       the genre of the book.
 * @param publishDate the publication date of the book.
 * @param language    the language of the book.
 * @param description a brief description of the book.
 * @param imageUrl    the URL of the book's cover image.
 * @param rating      the rating of the book.
 * @param authorId    the ID of the author associated with the book.
 */
public record BookDtoRequest(
    String title,
    String publisher,
    String isbn,
    Integer pages,
    String genre,
    LocalDate publishDate,
    String language,
    String description,
    String imageUrl,
    Double rating,
    Long authorId
) {}