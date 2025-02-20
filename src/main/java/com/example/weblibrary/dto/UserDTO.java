package com.example.weblibrary.dto;

/**
 * Data Transfer Object for User entity.
 * This class is not designed to be extended.
 */
public final class UserDTO {
    /**
     * The unique identifier for the user.
     */
    private int id;

    @Override
    public String toString() {
        return "UserDTO [id=" + id + "]";
    }
}

