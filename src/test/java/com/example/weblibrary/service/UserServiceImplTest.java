package com.example.weblibrary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.example.weblibrary.WebLibraryApplication;
import com.example.weblibrary.mapper.UserMapperImpl;
import com.example.weblibrary.model.User;
import com.example.weblibrary.model.dto.UserDtoRequest;
import com.example.weblibrary.model.dto.UserDtoResponse;
import com.example.weblibrary.repository.UserRepository;
import java.util.List;
import java.util.Optional;

import com.example.weblibrary.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = WebLibraryApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.name=application-test")
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapperImpl userMapper;

  @InjectMocks
  private UserServiceImpl userService;

  private User user;
  private UserDtoRequest userDtoRequest;
  private UserDtoResponse userDtoResponse;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setName("John Doe");

    userDtoRequest = new UserDtoRequest("John Doe");
    userDtoResponse = new UserDtoResponse(1L, "John Doe");
  }

  @Test
  void getAllUsers_ShouldReturnUserList() {
    when(userRepository.findAll()).thenReturn(List.of(user));
    when(userMapper.toUserDtoResponse(List.of(user))).thenReturn(List.of(userDtoResponse));

    List<UserDtoResponse> users = userService.getAll();

    assertThat(users).hasSize(1);
    assertThat(users.get(0).id()).isEqualTo(1L); // Use the field name directly (id())
    verify(userRepository).findAll();
  }

  @Test
  void getUserById_ShouldReturnUser() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userMapper.toUserDtoResponse(user)).thenReturn(userDtoResponse);

    UserDtoResponse foundUser = userService.getById(1L);

    assertThat(foundUser.id()).isEqualTo(1L); // Use the field name directly (id())
    verify(userRepository).findById(1L);
  }

  @Test
  void createUser_ShouldSaveUser() {
    when(userMapper.toUserEntity(userDtoRequest)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toUserDtoResponse(user)).thenReturn(userDtoResponse);

    UserDtoResponse createdUser = userService.create(userDtoRequest);

    assertThat(createdUser.id()).isEqualTo(1L); // Use the field name directly (id())
    verify(userRepository).save(user);
  }

  @Test
  void updateUser_ShouldUpdateUser() {
    when(userMapper.toUserEntity(userDtoRequest)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toUserDtoResponse(user)).thenReturn(userDtoResponse);

    UserDtoResponse updatedUser = userService.update(1L, userDtoRequest);

    assertThat(updatedUser.id()).isEqualTo(1L); // Use the field name directly (id())
    verify(userRepository).save(user);
  }

  @Test
  void deleteUser_ShouldRemoveUser() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    doNothing().when(userRepository).delete(user);

    userService.delete(1L);

    verify(userRepository).delete(user);
  }
}
