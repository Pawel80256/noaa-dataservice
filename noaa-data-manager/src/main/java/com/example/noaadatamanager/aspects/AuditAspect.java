package com.example.noaadatamanager.aspects;

import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.models.audit.MeasurementAudit;
import com.example.noaadatamanager.repository.audit.MeasurementAuditRepository;
import com.example.noaadatamanager.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Aspect
@Component
public class AuditAspect {
    private static final String SECRET_KEY = "mojBardzoTajnyKluczDoGenerowaniaTokenowJWT...";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private MeasurementAuditRepository measurementAuditRepository;
    private JwtService jwtService;

    public void setMeasurementAuditRepository(MeasurementAuditRepository measurementAuditRepository) {
        this.measurementAuditRepository = measurementAuditRepository;
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.NOAADataService.create(..))")
    public void createMeasurement(){}

    @After("createMeasurement()")
    public void addAuditRecord(JoinPoint joinPoint){
        System.out.println("after msrmnt creation");
        MeasurementInputDto inputDto = (MeasurementInputDto) List.of(joinPoint.getArgs()).getFirst();
        String measurementId = inputDto.getDataTypeId() + inputDto.getDate().toString() + inputDto.getStationId();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader("Authorization");

        String token = authorizationHeader.substring(7);

        MeasurementAudit measurementAudit = new MeasurementAudit.Builder()
                .recordId(measurementId)
                .operation("CREATE")
                .timestamp(LocalDateTime.now())
                .user(jwtService.getSubFromToken(token))
                .build();

        measurementAuditRepository.save(measurementAudit);
    }
}
