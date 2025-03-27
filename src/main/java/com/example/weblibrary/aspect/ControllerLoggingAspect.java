package com.example.weblibrary.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

  @Pointcut("within(com.example.weblibrary.controllers..*) && execution(* *(..))")
  public void controllerMethods() {}

  @Before("controllerMethods()")
  public void logBeforeControllerMethod(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();
    Object[] args = joinPoint.getArgs();

    log.info("Controller method called: {}.{}() with arguments: {}", className, methodName, args);
  }

  @AfterReturning(pointcut = "controllerMethods()", returning = "result")
  public void logAfterControllerMethod(JoinPoint joinPoint, Object result) {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();

    log.info("Controller method completed: {}.{}() with result: {}", className, methodName, result);
  }
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