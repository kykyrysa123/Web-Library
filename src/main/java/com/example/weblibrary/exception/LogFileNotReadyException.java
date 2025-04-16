package com.example.weblibrary.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к файлу журнала,
 * который еще не готов для чтения или скачивания.
 */
public class LogFileNotReadyException extends RuntimeException {

  /**
   * Создает новое исключение с указанным сообщением об ошибке.
   *
   * @param message сообщение, описывающее причину исключения
   */
  public LogFileNotReadyException(String message) {
    super(message);
  }
}