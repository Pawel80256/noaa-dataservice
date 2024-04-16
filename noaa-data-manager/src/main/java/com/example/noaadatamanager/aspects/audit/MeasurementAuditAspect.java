package com.example.noaadatamanager.aspects.audit;

import com.example.noaadatamanager.aspects.AspectUtils;
import com.example.noaadatamanager.entities.audit.MeasurementAudit;
import com.example.noaadatamanager.repository.audit.MeasurementAuditRepository;
import com.example.noaadatamanager.service.JwtService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class MeasurementAuditAspect {
    private MeasurementAuditRepository measurementAuditRepository;
    private JwtService jwtService;

    public void setMeasurementAuditRepository(MeasurementAuditRepository measurementAuditRepository) {
        this.measurementAuditRepository = measurementAuditRepository;
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.MeasurementService.create(..))")
    public void createMeasurement(){}

    @AfterReturning(pointcut = "createMeasurement()", returning = "measurementId")
    public void addCreateAudit(JoinPoint joinPoint, String measurementId){
        MeasurementAudit measurementAudit = new MeasurementAudit.Builder()
                .recordId(measurementId)
                .operation("CREATE")
                .timestamp(LocalDateTime.now())
                .user(jwtService.getSubFromToken(AspectUtils.extractTokenFromHeader()))
                .build();

        measurementAuditRepository.save(measurementAudit);
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.MeasurementService.delete(..))")
    public void deleteMeasurement(){}

    @AfterReturning("deleteMeasurement()")
    public void addDeleteAudit(JoinPoint joinPoint){
        String measurementId = joinPoint.getArgs()[0].toString();

        MeasurementAudit measurementAudit = new MeasurementAudit.Builder()
                .recordId(measurementId)
                .operation("DELETE")
                .timestamp(LocalDateTime.now())
                .user(jwtService.getSubFromToken(AspectUtils.extractTokenFromHeader()))
                .build();

        measurementAuditRepository.save(measurementAudit);
    }


}
