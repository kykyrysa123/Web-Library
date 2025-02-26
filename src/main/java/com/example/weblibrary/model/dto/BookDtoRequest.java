package com.example.weblibrary.model.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for representing book data in requests.
 * Used to encapsulate book information sent from the client to the server.
 *
 * @param title        the title of the book
 * @param publisher    the publisher of the book
 * @param isbn         the ISBN code of the book
 * @param pages        the number of pages in the book
 * @param genre        the genre of the book
 * @param publishDate  the publication date of the book
 * @param language     the language of the book
 * @param description  a brief description of the book
 * @param imageUrl     the URL of the book's cover image
 */
public record BookDtoRequest(String title, String publisher,
                             String isbn, Long pages, String genre,
                             LocalDate publishDate, String language,
                             String description, String imageUrl) {}