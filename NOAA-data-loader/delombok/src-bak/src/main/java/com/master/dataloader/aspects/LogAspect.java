package com.master.dataloader.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
public class LogAspect {

//    @Pointcut("execution(* com.master.dataloader..*.*(..))")
//    public void testPointcut(){
//
//    }
//
//    @After("testPointcut()")
//    public void elo(){
//        System.out.println("test");
//    }
    @Pointcut("execution(* com.master.dataloader.service.file_reading..*.*(..))")
    public void fileReadingPackagePointcut() {
    }

    @After("fileReadingPackagePointcut()")
    public void logInfo(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
//        log.info(methodName + " executed successfully");
        System.out.println(methodName + " executed successfully");
    }
}
