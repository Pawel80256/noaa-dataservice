package com.example.noaadatamanager.aspects.audit;

import com.example.noaadatamanager.aspects.AspectUtils;
import com.example.noaadatamanager.entities.audit.MeasurementAudit;
import com.example.noaadatamanager.entities.audit.StationAudit;
import com.example.noaadatamanager.repository.audit.MeasurementAuditRepository;
import com.example.noaadatamanager.repository.audit.StationAuditRepository;
import com.example.noaadatamanager.service.JwtService;
import com.example.noaadatamanager.service.MeasurementService;
import com.example.noaadatamanager.service.StationService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
public class AuditAspect {
    private StationAuditRepository stationAuditRepository;
    private MeasurementAuditRepository measurementAuditRepository;
    private JwtService jwtService;

    private Boolean exceptionOccurred = false;

    public void setStationAuditRepository(StationAuditRepository stationAuditRepository) {
        this.stationAuditRepository = stationAuditRepository;
    }

    public void setMeasurementAuditRepository(MeasurementAuditRepository measurementAuditRepository) {
        this.measurementAuditRepository = measurementAuditRepository;
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.*.create(..))")
    public void createMethods(){}
    @Pointcut("execution(* com.example.noaadatamanager.service.*.update*(..))")
    public void updateMethods(){}
    @Pointcut("execution(* com.example.noaadatamanager.service.*.delete(..))")
    public void deleteMethods(){}

    @AfterReturning(pointcut = "createMethods()", returning = "entityId")
    public void addCreateAudit(JoinPoint joinPoint, String entityId){
        String sourceServiceName = joinPoint.getSignature().getDeclaringTypeName();
        String resource = detectResource(sourceServiceName);

        switch (resource){
            case "STATION" -> {
                stationAuditRepository.save(
                        new StationAudit.Builder()
                                .recordId(entityId)
                                .operation("CREATE")
                                .timestamp(LocalDateTime.now())
                                .user(jwtService.getSubFromToken(extractTokenFromHeader()))
                                .build()
                );
            }
            case "MEASUREMENT" -> {
                measurementAuditRepository.save(
                        new MeasurementAudit.Builder()
                                .recordId(entityId)
                                .operation("CREATE")
                                .timestamp(LocalDateTime.now())
                                .user(jwtService.getSubFromToken(AspectUtils.extractTokenFromHeader()))
                                .build()
                );
            }
        }
    }

    @AfterReturning(pointcut = "updateMethods()", returning = "entityId")
    public void addUpdateAudit(JoinPoint joinPoint, String entityId){
        String sourceServiceName = joinPoint.getSignature().getDeclaringTypeName();
        String resource = detectResource(sourceServiceName);

        switch (resource){
            case "STATION" -> {
                stationAuditRepository.save(
                        new StationAudit.Builder()
                                .recordId(entityId)
                                .operation("UPDATE")
                                .timestamp(LocalDateTime.now())
                                .user(jwtService.getSubFromToken(extractTokenFromHeader()))
                                .build()
                );
            }
            case "MEASUREMENT" -> {
                measurementAuditRepository.save(
                        new MeasurementAudit.Builder()
                                .recordId(entityId)
                                .operation("UPDATE")
                                .timestamp(LocalDateTime.now())
                                .user(jwtService.getSubFromToken(AspectUtils.extractTokenFromHeader()))
                                .build()
                );
            }
        }
    }
    @AfterThrowing("deleteMethods()")
    public void handleException(/*JoinPoint joinPoint*/){
        exceptionOccurred = true;
    }

    @After("deleteMethods()")
     public void addDeleteAudit(JoinPoint joinPoint){
        try{
            if(!exceptionOccurred){
                String sourceServiceName = joinPoint.getSignature().getDeclaringTypeName();
                String resource = detectResource(sourceServiceName);
                String entityId = (String) joinPoint.getArgs()[0];
                switch (resource){
                    case "STATION" -> {
                        stationAuditRepository.save(
                                new StationAudit.Builder()
                                        .recordId(entityId)
                                        .operation("DELETE")
                                        .timestamp(LocalDateTime.now())
                                        .user(jwtService.getSubFromToken(extractTokenFromHeader()))
                                        .build()
                        );
                    }
                    case "MEASUREMENT" -> {
                        measurementAuditRepository.save(
                                new MeasurementAudit.Builder()
                                        .recordId(entityId)
                                        .operation("DELETE")
                                        .timestamp(LocalDateTime.now())
                                        .user(jwtService.getSubFromToken(AspectUtils.extractTokenFromHeader()))
                                        .build()
                        );
                    }
                }
            }
        }finally {
            exceptionOccurred = false;
        }
    }

    private String detectResource(String sourceServiceName){
        if(sourceServiceName != null && !sourceServiceName.isBlank()) {
            if (StationService.class.getName().equals(sourceServiceName)) {
                return  "STATION";
            }
            if (MeasurementService.class.getName().equals(sourceServiceName)){
                return "MEASUREMENT"; //todo: enum
            }
        }
        return "UNKNOWN";
    }

    private String extractTokenFromHeader(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader.substring(7);
    }
}