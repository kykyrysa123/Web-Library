package com.example.weblibrary.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging controller method calls, execution time and results.
 * This aspect provides logging before and after controller method execution,
 * as well as method execution time measurement.
 */
@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

  /**
   * Pointcut that matches all methods in controller classes.
   */
  @Pointcut("within(com.example.weblibrary.controllers..*) && execution(* *(..))")
  public void controllerMethods() {}

  /**
   * Logs controller method calls before execution.
   *
   * @param joinPoint the join point containing method information
   */
  @Before("controllerMethods()")
  public void logBeforeControllerMethod(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();
    Object[] args = joinPoint.getArgs();

    log.info("Controller method called: {}.{}() with arguments: {}",
        className, methodName, args);
  }

  /**
   * Logs controller method results after successful execution.
   *
   * @param joinPoint the join point containing method information
   * @param result the return value from the method
   */
  @AfterReturning(pointcut = "controllerMethods()", returning = "result")
  public void logAfterControllerMethod(JoinPoint joinPoint, Object result) {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();

    log.info("Controller method completed: {}.{}() with result: {}",
        className, methodName, result);
  }

  /**
   * Measures and logs controller method execution time.
   *
   * @param joinPoint the join point containing method information
   * @return the result of the method execution
   * @throws Throwable if the method execution throws an exception
   */
  @Around("controllerMethods()")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    long duration = System.currentTimeMillis() - startTime;

    log.info("Method {}.{}() executed in {} ms",
        joinPoint.getTarget().getClass().getSimpleName(),
        joinPoint.getSignature().getName(),
        duration);
    return result;
  }
}