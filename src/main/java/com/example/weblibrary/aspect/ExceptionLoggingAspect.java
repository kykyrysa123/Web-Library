package com.example.weblibrary.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging exceptions thrown by methods in the application.
 * This aspect intercepts exceptions and logs them with relevant context information.
 */
@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {

  /**
   * Logs exceptions thrown by any method in the application.
   *
   * @param joinPoint the join point containing method execution context
   * @param exception the exception that was thrown
   */
  @AfterThrowing(
      pointcut = "within(com.example.weblibrary..*)",
      throwing = "exception"
  )
  public void logException(JoinPoint joinPoint, Throwable exception) {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();

    log.error("Exception in {}.{}(): {}", className, methodName, exception.getMessage(), exception);
  }
}