package com.master.dataloader.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LogAspect {
    private final static Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* com.master.dataloader.service.NOAADataTypeService.getAll(..))")
    public void dataTypesGetAll() {
    }

    @Before("dataTypesGetAll()")
    public void beforeStandard(JoinPoint joinPoint) {
        log.info("========================================");
        log.info("Starting standard after execution pointcut: " + joinPoint.getSignature().toShortString());
        log.info("Proxy class: " + joinPoint.getThis().getClass().getName());
        log.info("Unique call identifier: " + System.identityHashCode(joinPoint));
        log.info("Thread name: " + Thread.currentThread().getName());
        log.info("========================================");
    }

    @Pointcut("@annotation(loggable)")
    public void loggableAnnotation(Loggable loggable) {
    }

    @Before("loggableAnnotation(loggable)")
    public void beforeLoggable(JoinPoint joinPoint, Loggable loggable) {
        log.info("========================================");
        log.info("Starting pointcut with @Loggable: " + joinPoint.getSignature().toShortString());
        log.info("Proxy class: " + joinPoint.getThis().getClass().getName());
        log.info("Unique call identifier: " + System.identityHashCode(joinPoint));
        log.info("Thread name: " + Thread.currentThread().getName());
        log.info("========================================");
    }

}
