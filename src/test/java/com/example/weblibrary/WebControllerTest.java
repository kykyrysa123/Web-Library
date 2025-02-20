package com.example.weblibrary;

import com.example.weblibrary.controllers.MainController;
import com.example.weblibrary.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebControllerTest {

  @Autowired
  private MainController controller; // Инжектируем MockMvc

  @Test
  void testGetUserPathVariableTest()  {
    Long id = 1L;
    String login = "testUser";
    String password = "testPassword";

    UserDto body = controller.getUserPathVariable(id, login,
        password).getBody();

    assert body != null && body.equals(
        new UserDto(id, login, password)
    );


//    // Выполняем GET-запрос с path variables и проверяем результат
//    controller.perform(get("/users/{id}/{login}/{password}", id, login, password)
//               .accept(MediaType.APPLICATION_JSON))
//           .andExpect(status().isOk())
//           .andExpect(content().json(objectMapper.writeValueAsString(expectedUserDto)));
  }

  @Test
  void testGetUserQueryVariableTest() {
    Long id = 1L;
    String login = "testUser";
    String password = "testPassword";

    UserDto body = controller.getUserPathVariable(id, login,
        password).getBody();

    assert body != null && body.equals(
        new UserDto(id, login, password)
    );

    // Выполняем GET-запрос с query parameters и проверяем результат
//    mockMvc.perform(get("/users")
//               .param("id", id.toString())
//               .param("login", login)
//               .param("password", password)
//               .accept(MediaType.APPLICATION_JSON))
//           .andExpect(status().isOk())
//           .andExpect(content().json(objectMapper.writeValueAsString(expectedUserDto)));
  }
}
