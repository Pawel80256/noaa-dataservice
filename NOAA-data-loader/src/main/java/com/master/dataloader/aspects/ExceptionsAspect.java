package com.master.dataloader.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

@Aspect
public class ExceptionsAspect {
    private final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* com.master.dataloader.service.*.delete*(..))")
    public void deleteOperation() {}

    @AfterThrowing(pointcut = "deleteOperation()", throwing = "ex")
    public void handleDataIntegrityViolationException(JoinPoint joinPoint, DataIntegrityViolationException ex) {
        log.info("Wyjątek DataIntegrityViolationException przechwycony dla metody: " + joinPoint.getSignature().getName());
    }
}
