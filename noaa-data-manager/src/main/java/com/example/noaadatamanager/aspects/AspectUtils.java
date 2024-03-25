package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AspectUtils {
    public static String getMethodMetadata(JoinPoint joinPoint){
        return joinPoint.getSignature().getDeclaringTypeName()
                + " " + joinPoint.getSignature().getName() + "(...)";
    }

    public static String extractTokenFromHeader(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || authorizationHeader.isBlank()){
            throw new ValidationException("Missing authorization token");
        }

        return authorizationHeader.substring(7);
    }
}
