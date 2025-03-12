package com.example.weblibrary;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.weblibrary.model.User;
import com.example.weblibrary.model.dto.UserDtoRequest;
import com.example.weblibrary.model.dto.UserDtoResponse;
import com.example.weblibrary.repository.UserRepository;
import com.example.weblibrary.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;

  private User user;
  private UserDtoRequest userDtoRequest;
  private UserDtoResponse userDtoResponse;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setName("John");
    user.setSurname("Doe");
    user.setEmail("john.doe@example.com");
    user.setAge(30);
    user.setSubscription("Premium");
    user.setSex("Male");
    user.setCountry("USA");
    user.setPasswordHash("hashed_password");
    user.setRegistrationDate(LocalDate.now());
    user.setLastLogin(LocalDate.now());

    userDtoRequest = new UserDtoRequest(
        1L, "John", "Doe", null, 30, "Premium", "Male", "USA",
        null, "john.doe@example.com", "hashed_password"
    );

    userDtoResponse = new UserDtoResponse(
        1L, "John", "Doe", null, 30, "Premium", "Male", "USA",
        null, "john.doe@example.com", "hashed_password", LocalDate.now(), LocalDate.now()
    );
  }

  @Test
  void getAllUsers_ShouldReturnListOfUsers() {
    when(userRepository.findAll()).thenReturn(List.of(user));
    List<UserDtoResponse> result = userService.getAll();
    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals(userDtoResponse.name(), result.get(0).name());
  }

  @Test
  void getUserById_ShouldReturnUser() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    UserDtoResponse result = userService.getById(1L);
    assertNotNull(result);
    assertEquals(userDtoResponse.name(), result.name());
  }

  @Test
  void getUserById_ShouldThrowException_WhenNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(RuntimeException.class, () -> userService.getById(1L));
  }

  @Test
  void createUser_ShouldReturnSavedUser() {
    when(userRepository.save(any(User.class))).thenReturn(user);
    UserDtoResponse result = userService.create(userDtoRequest);
    assertNotNull(result);
    assertEquals(userDtoResponse.name(), result.name());
  }

  @Test
  void updateUser_ShouldReturnUpdatedUser() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(user);
    UserDtoResponse result = userService.update(1L, userDtoRequest);
    assertNotNull(result);
    assertEquals(userDtoResponse.name(), result.name());
  }

  @Test
  void deleteUser_ShouldCallRepositoryDelete() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    doNothing().when(userRepository).delete(user);
    assertDoesNotThrow(() -> userService.delete(1L));
    verify(userRepository, times(1)).delete(user);
  }
}
