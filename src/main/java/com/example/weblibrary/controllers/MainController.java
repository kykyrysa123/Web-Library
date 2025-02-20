package com.example.weblibrary.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling main page requests and user-related operations.
 */
@RestController
public class MainController {

    /**
     * Handles requests for the home page.
     *
     * @param model
     *         The model to add attributes to for rendering the view.
     * @return The name of the view to render.
     */
    @GetMapping("/")
    public String home(final Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    /**
     * Retrieves user information based on path variables.
     *
     * @param id
     *         The user's ID.
     * @param login
     *         The user's login name.
     * @return A ResponseEntity with the user's ID and login.
     */
    @GetMapping("/users/{id}/{login}")
    public ResponseEntity<String> getUserPathVariable(
            @PathVariable final int id, @PathVariable final String login) {
        return ResponseEntity.ok(
                "(Path) We got userId: " + id + " and userLogin: " + login);
    }
    /**
     * Retrieves user information based on query parameters.
     *
     * @param id
     *         The user's ID.
     * @param login
     *         The user's login name.
     * @param password
     *         The user's password (Note: Handling passwords in query parameters
     *         is not secure).
     * @return A ResponseEntity with the user's ID, login, and password.
     */
    @GetMapping("/users")
    public ResponseEntity<String> getUserQueryVariable(
            @RequestParam final Long id, @RequestParam final String login,
            @RequestParam final Long password
    ) {
        return ResponseEntity.ok(
                "(Query) We got userId: " + id + " and userLogin: " + login
                        + " and password: " + password);
    }
}
