package com.example.noaadatamanager.aspects.authorization;

import com.example.noaadatamanager.annotations.RequestAuthorization;
import com.example.noaadatamanager.aspects.AspectUtils;
import com.example.noaadatamanager.exceptions.UnauthorizedAccessException;
import com.example.noaadatamanager.entities.Role;
import com.example.noaadatamanager.repository.RoleRepository;
import com.example.noaadatamanager.service.JwtService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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

        List<Role> userRoles = jwtService.getRolesFromToken(token);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequestAuthorization requestAuthorization = method.getAnnotation(RequestAuthorization.class);

        List<String> requiredRolesIds = Arrays.stream(requestAuthorization.roles()).toList();
        List<Role> requiredRoles = roleRepository.findAllById(requiredRolesIds);

        if (
                Collections.disjoint(
                        requiredRoles.stream().map(Role::getId).toList(),
                        userRoles.stream().map(Role::getId).toList()
                )
        ) {
            throw new UnauthorizedAccessException("Unauthorized access, required roles: [" +
                    String.join(", ", requiredRolesIds) +
                    "], user roles: [" +
                    String.join(", ", userRoles.stream().map(Role::getId).toList()) +
                    "]");
        }


    }
}
