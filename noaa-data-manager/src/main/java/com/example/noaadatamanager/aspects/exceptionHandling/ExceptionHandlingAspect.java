package com.example.noaadatamanager.aspects.exceptionHandling;

import com.example.noaadatamanager.exceptions.UnauthorizedAccessException;
import io.jsonwebtoken.ExpiredJwtException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ExceptionHandlingAspect {

    @Pointcut("execution(* com.example.noaadatamanager.*(..))")
    public void dataManagerApplication(){}
    @AfterThrowing(pointcut = "dataManagerApplication()", throwing = "ex")
    public void handleExpiredJwt(JoinPoint joinPoint, ExpiredJwtException ex){
        throw new UnauthorizedAccessException("Authorization token is expired");
    }
}
