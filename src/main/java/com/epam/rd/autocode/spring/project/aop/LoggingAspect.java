package com.epam.rd.autocode.spring.project.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("execution(* com.epam.rd.autocode.spring.project.service..*(..))")
    public void serviceMethod() {}

    @Before("serviceMethod()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Method: {} with args: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @AfterReturning(value = "serviceMethod()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Method: {} finished successfully, result: {}",
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(value = "serviceMethod()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("Method: {} threw: {}",
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }
}
