package com.example.noaadatacalculationmodule.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LogAspect {
    private final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * com.example.noaadatacalculationmodule.service.MeasurementCalcService.*(..))")
    public void measurementStatisticsMethods(){}

    @Before("measurementStatisticsMethods()")
    public void logMethodStarting(JoinPoint joinPoint){
        log.info("Starting " + joinPoint.getSignature().getName());
    }

    @AfterReturning("measurementStatisticsMethods()")
    public void logMethodFinish(JoinPoint joinPoint){
        log.info(joinPoint.getSignature().getName() + " finished successfully");
    }

}
