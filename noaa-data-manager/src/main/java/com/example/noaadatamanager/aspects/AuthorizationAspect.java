package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.annotations.RequestAuthorization;
import com.example.noaadatamanager.models.Role;
import com.example.noaadatamanager.repository.RoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


@Aspect
@Component
public class AuthorizationAspect {
    private static final String SECRET_KEY = "mojBardzoTajnyKluczDoGenerowaniaTokenowJWT...";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    private RoleRepository roleRepository;

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

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

            List<String> requiredRolesIds = Arrays.stream(requestAuthorization.roles()).toList();
            List<String> tokenRolesIds = (List<String>) claims.get("roles");

            List<Role> requiredRoles = roleRepository.findAllById(requiredRolesIds);
            List<Role> tokenRoles = roleRepository.findAllById(tokenRolesIds);

            System.out.println("Wymagane role: " + String.join(", ", requiredRoles.stream().map(Role::getId).toList()));
            System.out.println("Role użytkownika wykonującego requestt: " + String.join(", ", tokenRoles.stream().map(Role::getId).toList()));
        }

    }
}
