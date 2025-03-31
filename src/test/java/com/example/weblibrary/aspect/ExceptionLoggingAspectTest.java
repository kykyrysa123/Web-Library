package com.example.weblibrary.aspect;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExceptionLoggingAspectTest {

  @InjectMocks
  private ExceptionLoggingAspect exceptionLoggingAspect;

  @Mock
  private JoinPoint joinPoint;

  @Mock
  private Signature signature;

  @Mock
  private TestClass target; // Заменяем Object на TestClass

  // Мок для логгера
  @SuppressWarnings("unchecked")
  private Appender<ILoggingEvent> mockAppender;

  @BeforeEach
  void setUp() {
    // Настраиваем мок логгера
    Logger logger = (Logger) LoggerFactory.getLogger(ExceptionLoggingAspect.class);
    mockAppender = mock(Appender.class);
    logger.addAppender(mockAppender);
  }

  @Test
  void testLogException_WithExceptionMessage() {
    // Настраиваем моки
    when(joinPoint.getSignature()).thenReturn(signature);
    when(signature.getName()).thenReturn("testMethod");
    when(joinPoint.getTarget()).thenReturn(target);
    Throwable exception = new RuntimeException("Something went wrong");

    // Вызываем метод
    exceptionLoggingAspect.logException(joinPoint, exception);

    // Проверяем логирование
    ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
    verify(mockAppender).doAppend(logCaptor.capture());
    ILoggingEvent logEvent = logCaptor.getValue();
    assertEquals("ERROR", logEvent.getLevel().toString());
    assertEquals("Exception in TestClass.testMethod(): Something went wrong",
        logEvent.getFormattedMessage());
    assertNotNull(logEvent.getThrowableProxy()); // Проверяем наличие исключения
    assertEquals("Something went wrong", logEvent.getThrowableProxy().getMessage()); // Проверяем сообщение исключения
  }

  @Test
  void testLogException_WithNullMessage() {
    // Настраиваем моки
    when(joinPoint.getSignature()).thenReturn(signature);
    when(signature.getName()).thenReturn("testMethod");
    when(joinPoint.getTarget()).thenReturn(target);
    Throwable exception = new RuntimeException(); // Без сообщения

    // Вызываем метод
    exceptionLoggingAspect.logException(joinPoint, exception);

    // Проверяем логирование
    ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
    verify(mockAppender).doAppend(logCaptor.capture());
    ILoggingEvent logEvent = logCaptor.getValue();
    assertEquals("ERROR", logEvent.getLevel().toString());
    assertEquals("Exception in TestClass.testMethod(): null",
        logEvent.getFormattedMessage());
    assertNotNull(logEvent.getThrowableProxy()); // Проверяем наличие исключения
    assertNull(logEvent.getThrowableProxy().getMessage()); // Сообщение должно быть null
  }

  @Test
  void testLogException_WithCustomException() {
    // Настраиваем моки
    when(joinPoint.getSignature()).thenReturn(signature);
    when(signature.getName()).thenReturn("testMethod");
    when(joinPoint.getTarget()).thenReturn(target);
    Throwable exception = new IllegalArgumentException("Invalid input");

    // Вызываем метод
    exceptionLoggingAspect.logException(joinPoint, exception);

    // Проверяем логирование
    ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
    verify(mockAppender).doAppend(logCaptor.capture());
    ILoggingEvent logEvent = logCaptor.getValue();
    assertEquals("ERROR", logEvent.getLevel().toString());
    assertEquals("Exception in TestClass.testMethod(): Invalid input",
        logEvent.getFormattedMessage());
    assertNotNull(logEvent.getThrowableProxy()); // Проверяем наличие исключения
    assertEquals("Invalid input", logEvent.getThrowableProxy().getMessage()); // Проверяем сообщение исключения
  }

  // Вспомогательный класс для тестов
  private static class TestClass {
  }
}