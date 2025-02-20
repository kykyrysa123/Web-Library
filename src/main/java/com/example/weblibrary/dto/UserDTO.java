package com.example.weblibrary.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * Data Transfer Object for User entity.
 * This class is not designed to be extended.
 */
public final class UserDTO {
    /**
     * The unique identifier for the user.
     */
    private final Long id;

    /**
     * The first name of the user.
     */
    private final String name;

    /**
     * The last name of the user.
     */
    private final String password;

    public UserDTO(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "id=" + id + ", name='" + name + '\''
                + ", password='" + password + '\'' + '}';
    }
}
