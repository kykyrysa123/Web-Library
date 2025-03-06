package com.example.weblibrary.model.dto;

/**
 * Data Transfer Object (DTO) for representing book data in requests. Used to
 * encapsulate book information sent from the client to the server.
 *
 * @param pages
 *     the number of pages in the book
 * @param genre
 *     the genre of the book
 * @param language
 *     the language of the book
 */
public record BookDtoRequest(Long pages, String genre, String language) {}