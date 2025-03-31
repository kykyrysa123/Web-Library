package com.example.weblibrary.service.cache;

import static org.junit.jupiter.api.Assertions.*;

import com.example.weblibrary.WebLibraryApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.function.Consumer;
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = WebLibraryApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.name=application-test")
class SimpleCacheTest {

  private SimpleCache<String, String> cache;
  private static final int MAX_SIZE = 3;
  private Consumer<String> evictionListener;

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setUp() {
    evictionListener = Mockito.mock(Consumer.class);
    cache = new SimpleCache<>(MAX_SIZE);
    cache.setEvictionListener(evictionListener); // Устанавливаем слушатель через сеттер
  }

  @Test
  void testConstructor_InvalidMaxSize() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> new SimpleCache<>(0));
    assertEquals("Max size must be greater than 0", exception.getMessage());
  }

  @Test
  void testPutAndGet_NewItem() {
    cache.put("key1", "value1");

    assertEquals("value1", cache.get("key1"));
    assertEquals(1, cache.size());
  }

  @Test
  void testGet_IncreasesFrequency() {
    cache.put("key1", "value1");
    cache.get("key1"); // Первый вызов get
    cache.get("key1"); // Второй вызов get

    assertEquals("value1", cache.get("key1")); // Частота должна быть 3
    assertEquals(1, cache.size());
  }

  @Test
  void testGet_NonExistentKey() {
    assertNull(cache.get("nonexistent"));
    assertEquals(0, cache.size());
  }

  @Test
  void testPut_UpdateExistingKey() {
    cache.put("key1", "value1");
    cache.put("key1", "value2");

    assertEquals("value2", cache.get("key1"));
    assertEquals(1, cache.size());
  }

  @Test
  void testPut_EvictsLFUWhenFull() {
    cache.put("key1", "value1"); // frequency = 1
    cache.put("key2", "value2"); // frequency = 1
    cache.put("key3", "value3"); // frequency = 1
    cache.get("key2");           // frequency = 2 для key2
    cache.put("key4", "value4"); // Должен вытеснить key1 (min frequency = 1)

    assertNull(cache.get("key1")); // key1 вытеснен
    assertEquals("value2", cache.get("key2"));
    assertEquals("value3", cache.get("key3"));
    assertEquals("value4", cache.get("key4"));
    assertEquals(MAX_SIZE, cache.size());
    Mockito.verify(evictionListener).accept("key1"); // Проверка вызова слушателя
  }

  @Test
  void testRemove_ExistingKey() {
    cache.put("key1", "value1");
    String removedValue = cache.remove("key1");

    assertEquals("value1", removedValue);
    assertNull(cache.get("key1"));
    assertEquals(0, cache.size());
    Mockito.verify(evictionListener).accept("key1"); // Проверка вызова слушателя
  }

  @Test
  void testRemove_NonExistentKey() {
    String removedValue = cache.remove("nonexistent");

    assertNull(removedValue);
    assertEquals(0, cache.size());
    Mockito.verifyNoInteractions(evictionListener); // Слушатель не вызывается
  }

  @Test
  void testClear() {
    cache.put("key1", "value1");
    cache.put("key2", "value2");
    cache.clear();

    assertEquals(0, cache.size());
    assertNull(cache.get("key1"));
    assertNull(cache.get("key2"));
    Mockito.verifyNoInteractions(evictionListener); // Слушатель не вызывается при clear
  }

  @Test
  void testSize() {
    assertEquals(0, cache.size());
    cache.put("key1", "value1");
    assertEquals(1, cache.size());
    cache.put("key2", "value2");
    assertEquals(2, cache.size());
  }

  @Test
  void testToString() {
    cache.put("key1", "value1");
    String cacheString = cache.toString();

    assertTrue(cacheString.contains("key1"));

  }
}