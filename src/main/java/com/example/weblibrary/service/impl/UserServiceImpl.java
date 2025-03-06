package com.example.weblibrary.service.impl;

import com.example.weblibrary.model.User;
import com.example.weblibrary.repository.UserRepository;
import com.example.weblibrary.service.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements CrudService<User> {

  private final UserRepository userRepository;

  // Конструктор для внедрения зависимостей
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public List<User> getAll() {
    return userRepository.findAll();  // Возвращаем всех пользователей
  }

  @Override
  public Optional<User> getById(int id) {
    return userRepository.findById(
        id);  // Ищем пользователя по id, приводим к Long
  }

  @Override
  public User create(User user) {
    return userRepository.save(user);  // Сохраняем нового пользователя
  }

  @Override
  public User update(int id, User user) {
    if (userRepository.existsById(id)) {  // Проверяем, существует ли пользователь с таким id
      user.setId(id);  // Устанавливаем id пользователя
      return userRepository.save(user);  // Обновляем пользователя
    } else {
      throw new RuntimeException("User not found with id: " + id);  // Если пользователь не найден
    }
  }

  @Override
  public void delete(int id) {
    if (userRepository.existsById(
        id)) {  // Проверяем, существует ли пользователь с таким id
      userRepository.deleteById((id));  // Удаляем пользователя
    } else {
      throw new RuntimeException(
          "User not found with id: " + id);  // Если пользователь не найден
    }
  }
}
