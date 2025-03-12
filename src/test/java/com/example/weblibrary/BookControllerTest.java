package com.example.weblibrary.controllers;

import com.example.weblibrary.model.dto.BookDtoRequest;
import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.service.impl.BookServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

  @Mock
  private BookServiceImpl bookService;

  @InjectMocks
  private BookController bookController;

  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private BookDtoResponse bookDtoResponse;
  private BookDtoRequest bookDtoRequest;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

    bookDtoResponse = new BookDtoResponse(
        1L, "Test Book", "Test Publisher",
        "1234567890", 300L, "Fiction",
        LocalDate.of(2020, 1, 1), "English",
        "A test description", "http://example.com/image",
        null
    );

    bookDtoRequest = new BookDtoRequest(
        "Test Book", "Test Publisher",
        "1234567890", 300, "Fiction",
        LocalDate.of(2020, 1, 1), "English",
        "A test description", "http://example.com/image",
        4.5, 1L
    );
  }

  @Test
  void getAllBooks_ShouldReturnListOfBooks() throws Exception {
    Mockito.when(bookService.getAll()).thenReturn(List.of(bookDtoResponse));

    mockMvc.perform(get("/api/books"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.size()").value(1))
           .andExpect(jsonPath("$[0].title").value("Test Book"));
  }

  @Test
  void getBookById_ShouldReturnBook() throws Exception {
    Mockito.when(bookService.getById(anyLong())).thenReturn(bookDtoResponse);

    mockMvc.perform(get("/api/books/{id}", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.title").value("Test Book"));
  }

  @Test
  void createBook_ShouldReturnCreatedBook() throws Exception {
    Mockito.when(bookService.create(any(BookDtoRequest.class))).thenReturn(bookDtoResponse);

    mockMvc.perform(post("/api/books")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(bookDtoRequest)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.title").value("Test Book"));
  }

  @Test
  void updateBook_ShouldReturnUpdatedBook() throws Exception {
    Mockito.when(bookService.update(anyLong(), any(BookDtoRequest.class))).thenReturn(bookDtoResponse);

    mockMvc.perform(put("/api/books/{id}", 1L)
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(bookDtoRequest)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.title").value("Test Book"));
  }

  @Test
  void deleteBook_ShouldReturnNoContent() throws Exception {
    Mockito.doNothing().when(bookService).delete(anyLong());

    mockMvc.perform(delete("/api/books/{id}", 1L))
           .andExpect(status().isNoContent());
  }

  @Test
  void getBooksByGenre_ShouldReturnFilteredBooks() throws Exception {
    Mockito.when(bookService.getByGenre(any())).thenReturn(Collections.singletonList(bookDtoResponse));

    mockMvc.perform(get("/api/books/genre")
               .param("genre", "Fiction"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.size()").value(1))
           .andExpect(jsonPath("$[0].genre").value("Fiction"));
  }
}
