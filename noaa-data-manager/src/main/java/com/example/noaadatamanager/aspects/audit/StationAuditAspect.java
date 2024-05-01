package com.example.noaadatamanager.aspects.audit;

import com.example.noaadatamanager.entities.audit.StationAudit;
import com.example.noaadatamanager.repository.audit.StationAuditRepository;
import com.example.noaadatamanager.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
public class StationAuditAspect {
    private StationAuditRepository stationAuditRepository;
    private JwtService jwtService;

    public void setStationAuditRepository(StationAuditRepository stationAuditRepository) {
        this.stationAuditRepository = stationAuditRepository;
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.StationService.create(..))")
    public void createStation(){}

    @AfterReturning(pointcut = "createStation()", returning = "stationId")
    public void addCreateAudit(JoinPoint joinPoint, String stationId){
        StationAudit stationAudit = new StationAudit.Builder()
                .recordId(stationId)
                .operation("CREATE")
                .timestamp(LocalDateTime.now())
                .user(jwtService.getSubFromToken(extractTokenFromHeader()))
                .build();

//        stationAuditRepository.save(stationAudit);
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.StationService.delete(..))")
    public void deleteStation(){}

    @AfterReturning("deleteStation()")
    public void addDeleteAudit(JoinPoint joinPoint){
        String stationId = joinPoint.getArgs()[0].toString();

        StationAudit stationAudit = new StationAudit.Builder()
                .recordId(stationId)
                .operation("DELETE")
                .timestamp(LocalDateTime.now())
                .user(jwtService.getSubFromToken(extractTokenFromHeader()))
                .build();

//        stationAuditRepository.save(stationAudit);
    }

    private String extractTokenFromHeader(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader.substring(7);
    }
}
