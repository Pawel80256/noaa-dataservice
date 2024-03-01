package com.master.dataloader.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LogAspect {
        private final Logger log = LoggerFactory.getLogger(LogAspect.class);

//    @Pointcut("execution(* com.master.dataloader..*.*(..))")
//    public void testPointcut(){
//
//    }
//
//    @After("testPointcut()")
//    public void elo(){
//        System.out.println("test");
//    }
    @Pointcut("execution(public * com.master.dataloader.service..*.*(..))")
    public void publicServiceMethod() {
    }

    @After("publicServiceMethod()")
    public void logInfo(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        log.info(methodName + " executed successfully");
    }
}
