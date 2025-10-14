package com.epam.rd.autocode.spring.project.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class StructuredLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(StructuredLoggingAspect.class);

    @Pointcut("within(com.epam.rd.autocode.spring.project.service.impl..*) || " +
            "within(com.epam.rd.autocode.spring.project.controller..*)")
    public void applicationPackagePointcut() {}

    @Pointcut("@annotation(loggableBusinessEvent)")
    public void businessEventPointcut(LoggableBusinessEvent loggableBusinessEvent) {}

    @Pointcut("@annotation(loggableSecurityEvent)")
    public void securityEventPointcut(LoggableSecurityEvent loggableSecurityEvent) {}

    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "e")
    public void logError(JoinPoint joinPoint, Throwable e) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        logger.error(
                "[ERROR] Виняток у методі: {}. Аргументи: {}. Помилка: {}",
                methodName, Arrays.toString(args), e.getMessage(), e
        );
    }

    @Around(value = "businessEventPointcut(loggableEvent)", argNames = "joinPoint,loggableEvent")
    public Object logBusinessEvent(ProceedingJoinPoint joinPoint, LoggableBusinessEvent loggableEvent) throws Throwable {
        String eventDescription = loggableEvent.value();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        logger.info("[BUSINESS EVENT] Початок: '{}'. Метод: {}. Аргументи: {}",
                eventDescription, methodName, Arrays.toString(args));

        Object result = joinPoint.proceed();

        if (joinPoint.getSignature() instanceof org.aspectj.lang.reflect.MethodSignature) {
            org.aspectj.lang.reflect.MethodSignature signature = (org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature();
            if (signature.getReturnType() == void.class) {
                logger.info("[BUSINESS EVENT] Успішно: '{}'. Метод: {} завершився.", eventDescription, methodName);
            } else {
                logger.info("[BUSINESS EVENT] Успішно: '{}'. Результат: {}", eventDescription, result);
            }
        }

        return result;
    }

    @Around(value = "securityEventPointcut(securityEvent)", argNames = "joinPoint,securityEvent")
    public Object logSecurityEvent(ProceedingJoinPoint joinPoint, LoggableSecurityEvent securityEvent) throws Throwable {
        Object result = joinPoint.proceed();

        String eventDescription = securityEvent.value();
        Level level = securityEvent.level();
        String methodName = joinPoint.getSignature().toShortString();

        String userContext = getCurrentUserContext();

        String message = String.format("[SECURITY EVENT] Успіх: '%s'. Контекст: [%s]. Метод: %s",
                eventDescription, userContext, methodName);

        logWithLevel(level, message);

        return result;
    }

    private String getCurrentUserContext() {
        String username = "анонім";
        String ipAddress = "невідомий IP";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
        }

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                ipAddress = request.getRemoteAddr();
            }
        } catch (IllegalStateException e) {
            logger.trace("Не вдалося отримати HTTP-запит для логування IP-адреси.");
        }

        return String.format("користувач=%s, ip=%s", username, ipAddress);
    }

    private void logWithLevel(Level level, String message) {
        switch (level) {
            case ERROR: logger.error(message); break;
            case WARN: logger.warn(message); break;
            case DEBUG: logger.debug(message); break;
            case TRACE: logger.trace(message); break;
            case INFO:
            default:
                logger.info(message); break;
        }
    }
}