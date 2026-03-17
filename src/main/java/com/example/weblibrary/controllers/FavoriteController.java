package com.example.weblibrary.controllers;

import com.example.weblibrary.dto.FavoriteDto;
import com.example.weblibrary.model.Book;
import com.example.weblibrary.model.Favorite;
import com.example.weblibrary.model.User;
import com.example.weblibrary.repository.BookRepository;
import com.example.weblibrary.repository.FavoriteRepository;
import com.example.weblibrary.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    // Получить все избранное текущего пользователя
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyFavorites() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());
            List<Book> books = favorites.stream()
                    .map(Favorite::getBook)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка получения избранного: " + e.getMessage());
        }
    }

    // Получить избранное по ID пользователя (для админа)
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<?> getFavoritesByUserId(@PathVariable Long userId) {
        try {
            List<Favorite> favorites = favoriteRepository.findByUserId(userId);
            List<Book> books = favorites.stream()
                    .map(Favorite::getBook)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка получения избранного: " + e.getMessage());
        }
    }

    // Добавить в избранное
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addToFavorites(@RequestBody Map<String, Long> request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Long bookId = request.get("bookId");
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndBookId(user.getId(), bookId);

            if (existingFavorite.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Книга уже в избранном");
            }

            Favorite favorite = new Favorite(user, book);
            favoriteRepository.save(favorite);

            return ResponseEntity.ok("Книга добавлена в избранное");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка добавления в избранное: " + e.getMessage());
        }
    }

    // Удалить из избранного
    @DeleteMapping("/{bookId}")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> removeFromFavorites(@PathVariable Long bookId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Проверяем существование избранного
            if (!favoriteRepository.existsByUserIdAndBookId(user.getId(), bookId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Книга не найдена в избранном");
            }

            // Удаляем
            favoriteRepository.deleteByUserIdAndBookId(user.getId(), bookId);

            return ResponseEntity.ok("Книга удалена из избранного");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка удаления из избранного: " + e.getMessage());
        }
    }

    // Проверить, есть ли книга в избранном
    @GetMapping("/check/{bookId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> checkFavorite(@PathVariable Long bookId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            boolean isFavorite = favoriteRepository.existsByUserIdAndBookId(user.getId(), bookId);
            return ResponseEntity.ok(isFavorite);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка проверки избранного: " + e.getMessage());
        }
    }
}