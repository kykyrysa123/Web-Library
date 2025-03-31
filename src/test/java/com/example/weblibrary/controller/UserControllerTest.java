package com.example.weblibrary.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.weblibrary.controllers.UserController;
import com.example.weblibrary.model.dto.UserDtoRequest;
import com.example.weblibrary.model.dto.UserDtoResponse;
import com.example.weblibrary.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @InjectMocks
  private UserController userController;

  @Mock
  private UserServiceImpl userService;

  private UserDtoRequest userRequest;
  private UserDtoResponse userResponse;

  @BeforeEach
  void setUp() {
    userRequest = new UserDtoRequest(1L, "Test", "User", "Description",30, "male", "Belarus", "test@example.com","asd","asd", "asdc");
    userResponse = new UserDtoResponse(1L, "testuser", "Test", "User", 30, "Description", "male", "Belarus", "test@example.com","asd","asd", LocalDate.now(), LocalDate.now());
  }

  @Test
  void testGetAllUsers_Success() {
    List<UserDtoResponse> users = List.of(userResponse);
    when(userService.getAll()).thenReturn(users);

    ResponseEntity<List<UserDtoResponse>> response = userController.getAllUsers();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(users, response.getBody());
  }

  @Test
  void testGetUserById_Success() {
    Long id = 1L;
    when(userService.getById(id)).thenReturn(userResponse);

    ResponseEntity<UserDtoResponse> response = userController.getUserById(id);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userResponse, response.getBody());
  }

  @Test
  void testCreateUser_Success() {
    when(userService.create(userRequest)).thenReturn(userResponse);

    ResponseEntity<UserDtoResponse> response = userController.createUser(userRequest);

    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(userResponse, response.getBody());
  }

  @Test
  void testUpdateUser_Success() {
    Long id = 1L;
    when(userService.update(id, userRequest)).thenReturn(userResponse);

    ResponseEntity<UserDtoResponse> response = userController.updateUser(id, userRequest);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userResponse, response.getBody());
  }

  @Test
  void testDeleteUser_Success() {
    Long id = 1L;
    doNothing().when(userService).delete(id);

    ResponseEntity<Void> response = userController.deleteUser(id);

    assertNotNull(response);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }
}