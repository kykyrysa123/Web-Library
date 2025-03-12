package com.example.weblibrary;

import com.example.weblibrary.controllers.ReviewController;
import com.example.weblibrary.model.dto.BookDtoResponse;
import com.example.weblibrary.model.dto.ReviewDtoRequest;
import com.example.weblibrary.model.dto.ReviewDtoResponse;
import com.example.weblibrary.model.dto.UserDtoResponse;
import com.example.weblibrary.service.impl.ReviewServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @Mock
  private ReviewServiceImpl reviewService;

  @InjectMocks
  private ReviewController reviewController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void getAllReviews_shouldReturnListOfReviews() throws Exception {
    List<ReviewDtoResponse> reviews = List.of(
        new ReviewDtoResponse(
        1L,
        new BookDtoResponse(1L, "Book Title", "Publisher", "1234567890", 300L, "Fiction",
            LocalDate.now(), "English", "Description", "image.jpg", null),
        new UserDtoResponse(1L, "John Doe", "johndoe@example.com","asd",1200,"asd","sex", "country", "favoriteBooks", "email", "pas", LocalDate.now(), LocalDate.now() ),
        4.5,
        "Great book!",
        LocalDate.now()
    ));

    when(reviewService.getAll()).thenReturn(reviews);

    mockMvc.perform(get("/api/reviews"))
           .andExpect(status().isOk())
           .andExpect(content().json(objectMapper.writeValueAsString(reviews)));

    verify(reviewService, times(1)).getAll();
  }

  @Test
  void getReviewById_shouldReturnReview() throws Exception {
    ReviewDtoResponse review = new ReviewDtoResponse(
        1L,
        new BookDtoResponse(1L, "Book Title", "Publisher", "1234567890", 300L, "Fiction",
            LocalDate.now(), "English", "Description", "image.jpg", null),
        new UserDtoResponse(1L, "John Doe", "johndoe@example.com","asd",13,"asd","m","russian","nont","stasukarteem@gmail.com","cxv",LocalDate.now(),LocalDate.now()),
        4.5,
        "Great book!",
        LocalDate.now()
    );

    when(reviewService.getById(1L)).thenReturn(review);

    mockMvc.perform(get("/api/reviews/1"))
           .andExpect(status().isOk())
           .andExpect(content().json(objectMapper.writeValueAsString(review)));

    verify(reviewService, times(1)).getById(1L);
  }

  @Test
  void createReview_shouldReturnCreatedReview() throws Exception {
    ReviewDtoRequest reviewRequest = new ReviewDtoRequest(1L, 1L, 1L, 4.5, "Great book!", LocalDate.now());
    ReviewDtoResponse createdReview = new ReviewDtoResponse(
        1L,
        new BookDtoResponse(1L, "Book Title", "Publisher", "1234567890", 300L, "Fiction",
            LocalDate.now(), "English", "Description", "image.jpg", null),
        new UserDtoResponse(1L, "John Doe", "johndoe@example.com","asd",12,"asd","zxcv","xcv","xcb","ertyt","dgfg",LocalDate.now(),LocalDate.now()),
        4.5,
        "Great book!",
        LocalDate.now()
    );

    when(reviewService.create(any(ReviewDtoRequest.class))).thenReturn(createdReview);

    mockMvc.perform(post("/api/reviews")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(reviewRequest)))
           .andExpect(status().isCreated())
           .andExpect(content().json(objectMapper.writeValueAsString(createdReview)));

    verify(reviewService, times(1)).create(any(ReviewDtoRequest.class));
  }

  @Test
  void updateReview_shouldReturnUpdatedReview() throws Exception {
    ReviewDtoRequest reviewUpdate = new ReviewDtoRequest(1L, 1L, 1L, 4.8, "Amazing book!", LocalDate.now());
    ReviewDtoResponse updatedReview = new ReviewDtoResponse(
        1L,
        new BookDtoResponse(1L, "Book Title", "Publisher", "1234567890", 300L, "Fiction",
            LocalDate.now(), "English", "Description", "image.jpg", null),
        new UserDtoResponse(1L, "John Doe", "johndoe@example.com","asd",12,"xcv","m","russia","pomer","stast@gmaI.COM","ASDAF",LocalDate.now(), LocalDate.now()),
        4.8,
        "Amazing book!",
        LocalDate.now()
    );

    when(reviewService.update(eq(1L), any(ReviewDtoRequest.class))).thenReturn(updatedReview);

    mockMvc.perform(put("/api/reviews/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(reviewUpdate)))
           .andExpect(status().isOk())
           .andExpect(content().json(objectMapper.writeValueAsString(updatedReview)));

    verify(reviewService, times(1)).update(eq(1L), any(ReviewDtoRequest.class));
  }

  @Test
  void deleteReview_shouldReturnNoContent() throws Exception {
    doNothing().when(reviewService).delete(1L);

    mockMvc.perform(delete("/api/reviews/1"))
           .andExpect(status().isNoContent());

    verify(reviewService, times(1)).delete(1L);
  }
}
