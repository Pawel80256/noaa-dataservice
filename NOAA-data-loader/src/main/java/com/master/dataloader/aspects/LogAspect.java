package com.master.dataloader.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LogAspect {
    private final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("within(com.master.dataloader.controller..*)")
    public void controllers() {
    }
    @Pointcut("execution(* com.master.dataloader.controller..*.*(..))")
    public void controllerMethods(){}
//&& !execution(public *.new(..))
//     && !(execution(*.new(..)))
    @Around("controllerMethods()")
    public Object logEndpointExecutionTime(ProceedingJoinPoint joinPoint) {
        log.info("starting " + joinPoint.getSignature().toShortString());
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        log.info("executed " + joinPoint.getSignature().toShortString());
        return result;
    }
}
