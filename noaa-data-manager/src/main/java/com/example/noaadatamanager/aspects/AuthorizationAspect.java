package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.annotations.RequestAuthorization;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Aspect
public class AuthorizationAspect {
    private static final String SECRET_KEY = "mojBardzoTajnyKluczDoGenerowaniaTokenowJWT...";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    @Pointcut("@annotation(com.example.noaadatamanager.annotations.RequestAuthorization)")
    public void authorizedMethods() {}

    @Before("authorizedMethods()")
    public void test(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && !authorizationHeader.isBlank() && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            RequestAuthorization requestAuthorization = method.getAnnotation(RequestAuthorization.class);
            String[] requiredRoles = requestAuthorization.roles();

            System.out.println("Wymagane role: " + String.join(", ", requiredRoles));

            Object tokenRoles = claims.get("roles");
            System.out.println("Role użytkownika wykonującego requestt: " + tokenRoles);
            
        }
    }
}
