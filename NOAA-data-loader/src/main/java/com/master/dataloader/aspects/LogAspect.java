package com.master.dataloader.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
public class LogAspect {
    private final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* com.master.dataloader.controller..*.*(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logControllerMethod(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String path = attributes != null ? attributes.getRequest().getRequestURI() : "unknown";
        String methodName = joinPoint.getSignature().toShortString();

        long startTime = System.currentTimeMillis();
        log.info("Starting {} for path {}", methodName, path);

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();

            log.debug("Successfully finished {} in {} ms", methodName, (endTime - startTime));
            return result;
        } catch (Throwable e) {
            log.error("Method {} thrown exception: {}", methodName, e.getMessage());
            throw new RuntimeException(e); //todo: handle
        }
    }

    @Pointcut("execution(public * com.master.dataloader.service..*.*(..))")
    public void publicServiceMethods() {
    }

    @Around("publicServiceMethods()")
    public Object logServiceMethod(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("Starting {}", methodName);

        try {
            Object result = joinPoint.proceed();
            log.info("Successfully finished {}", methodName);
            return result;
        } catch (Throwable e) {
            throw new RuntimeException(e); //todo: handle
        }
    }

    @Pointcut("execution(private * com.master.dataloader.utils.Utils.sendRequest(..))")
    public void remoteApiCall() {
    }

    @Around("remoteApiCall()")
    public Object logRemoteApiCall(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("Starting remote api call {} with params {} ", args[0], args[1]);

        try {
            Object result = joinPoint.proceed();
            log.info("{} {} finished successfully", args[0], args[1]);
            return result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
