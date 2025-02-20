package com.example.weblibrary;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.weblibrary.controllers.MainController;
import com.example.weblibrary.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MainController.class)
public class WebControllerTest {

  @Autowired
  private MockMvc mockMvc; // Инжектируем MockMvc

  @Autowired
  private ObjectMapper objectMapper; // Инжектируем ObjectMapper для работы с JSON

  @Test
  void testGetUserPathVariableTest() throws Exception {
    Long id = 1L;
    String login = "testUser";
    String password = "testPassword";

    UserDto expectedUserDto = new UserDto(id, login, password);

    // Выполняем GET-запрос с path variables и проверяем результат
    mockMvc.perform(get("/users/{id}/{login}/{password}", id, login, password)
               .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().json(objectMapper.writeValueAsString(expectedUserDto)));
  }

  @Test
  void testGetUserQueryVariableTest() throws Exception {
    Long id = 1L;
    String login = "testUser";
    String password = "testPassword";

    UserDto expectedUserDto = new UserDto(id, login, password);

    // Выполняем GET-запрос с query parameters и проверяем результат
    mockMvc.perform(get("/users")
               .param("id", id.toString())
               .param("login", login)
               .param("password", password)
               .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().json(objectMapper.writeValueAsString(expectedUserDto)));
  }
}
