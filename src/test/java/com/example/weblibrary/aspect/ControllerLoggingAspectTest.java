package com.example.weblibrary.aspect;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerLoggingAspectTest {

  @InjectMocks
  private ControllerLoggingAspect loggingAspect;

  @Mock
  private JoinPoint joinPoint;

  @Mock
  private ProceedingJoinPoint proceedingJoinPoint;

  @Mock
  private Signature signature;

  @Mock
  private TestController target;

  // Мок для логгера
  @SuppressWarnings("unchecked")
  private Appender<ILoggingEvent> mockAppender;

  @BeforeEach
  void setUp() {
    // Настраиваем мок логгера
    Logger logger = (Logger) LoggerFactory.getLogger(ControllerLoggingAspect.class);
    mockAppender = mock(Appender.class);
    logger.addAppender(mockAppender);
  }

  @Test
  void testLogBeforeControllerMethod() {
    // Настраиваем моки
    when(joinPoint.getSignature()).thenReturn(signature);
    when(signature.getName()).thenReturn("testMethod");
    when(joinPoint.getTarget()).thenReturn(target);
    when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1", "arg2"});

    // Вызываем метод
    loggingAspect.logBeforeControllerMethod(joinPoint);

    // Проверяем логирование
    ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
    verify(mockAppender).doAppend(logCaptor.capture());
    ILoggingEvent logEvent = logCaptor.getValue();
    assertEquals("INFO", logEvent.getLevel().toString());
    assertEquals("Controller method called: TestController.testMethod() with arguments: [arg1, arg2]",
        logEvent.getFormattedMessage());
  }

  @Test
  void testLogAfterControllerMethod() {
    // Настраиваем моки
    when(joinPoint.getSignature()).thenReturn(signature);
    when(signature.getName()).thenReturn("testMethod");
    when(joinPoint.getTarget()).thenReturn(target);
    Object result = "resultValue";

    // Вызываем метод
    loggingAspect.logAfterControllerMethod(joinPoint, result);

    // Проверяем логирование
    ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
    verify(mockAppender).doAppend(logCaptor.capture());
    ILoggingEvent logEvent = logCaptor.getValue();
    assertEquals("INFO", logEvent.getLevel().toString());
    assertEquals("Controller method completed: TestController.testMethod() with result: resultValue",
        logEvent.getFormattedMessage());
  }

  @Test
  void testLogExecutionTime() throws Throwable {
    // Настраиваем моки
    when(proceedingJoinPoint.getSignature()).thenReturn(signature);
    when(signature.getName()).thenReturn("testMethod");
    when(proceedingJoinPoint.getTarget()).thenReturn(target);
    when(proceedingJoinPoint.proceed()).thenReturn("resultValue");

    // Вызываем метод
    Object result = loggingAspect.logExecutionTime(proceedingJoinPoint);

    // Проверяем результат
    assertEquals("resultValue", result);

    // Проверяем логирование
    ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
    verify(mockAppender).doAppend(logCaptor.capture());
    ILoggingEvent logEvent = logCaptor.getValue();
    assertEquals("INFO", logEvent.getLevel().toString());
    String logMessage = logEvent.getFormattedMessage();
    assertTrue(logMessage.startsWith("Method TestController.testMethod() executed in "));
    assertTrue(logMessage.endsWith(" ms"));
    // Проверяем, что время выполнения >= 0
    String timePart = logMessage.split(" ")[4]; // Исправлено: берем индекс 4
    assertTrue(Long.parseLong(timePart) >= 0);
  }

  // Вспомогательный класс для тестов
  private static class TestController {
  }
}