package com.example.noaadatamanager.aspects.authorization;

import com.example.noaadatamanager.annotations.RequestAuthorization;
import com.example.noaadatamanager.aspects.AspectUtils;
import com.example.noaadatamanager.exceptions.UnauthorizedAccessException;
import com.example.noaadatamanager.models.Role;
import com.example.noaadatamanager.repository.RoleRepository;
import com.example.noaadatamanager.service.JwtService;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Aspect
@Component
public class AuthorizationAspect {
    private RoleRepository roleRepository;
    private JwtService jwtService;

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Pointcut("@annotation(com.example.noaadatamanager.annotations.RequestAuthorization)")
    public void authorizedMethods() {}

    @Before("authorizedMethods()")
    public void test(JoinPoint joinPoint) {
        String token = AspectUtils.extractTokenFromHeader();
        List<Role> tokenRoles = jwtService.getRolesFromToken(token);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequestAuthorization requestAuthorization = method.getAnnotation(RequestAuthorization.class);

        List<String> requiredRolesIds = Arrays.stream(requestAuthorization.roles()).toList();
        List<Role> requiredRoles = roleRepository.findAllById(requiredRolesIds);

        if (
                Collections.disjoint(
                        requiredRoles.stream().map(Role::getId).toList(),
                        tokenRoles.stream().map(Role::getId).toList()
                )
        ) {
            throw new UnauthorizedAccessException("Unauthorized access, required roles: [" +
                    String.join(", ", requiredRolesIds) +
                    "], user roles: [" +
                    String.join(", ", tokenRoles.stream().map(Role::getId).toList()) +
                    "]");
        }


    }
}
