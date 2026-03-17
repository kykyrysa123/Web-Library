package com.example.weblibrary.dto;

public class FavoriteDto {
    private Long userId;
    private Long bookId;

    // Constructors
    public FavoriteDto() {}

    public FavoriteDto(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}