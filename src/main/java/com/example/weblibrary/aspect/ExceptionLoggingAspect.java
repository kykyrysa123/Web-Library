package com.example.weblibrary.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {

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