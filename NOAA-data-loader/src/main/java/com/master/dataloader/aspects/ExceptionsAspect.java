package com.master.dataloader.aspects;

import com.master.dataloader.exceptions.ResourceInUseException;
import com.master.dataloader.service.NOAALocationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
public class ExceptionsAspect {
    private final Logger log = LoggerFactory.getLogger(ExceptionsAspect.class);

    @Pointcut("execution(* com.master.dataloader.service.*.delete*(..))")
    public void deleteOperation() {}

    @AfterThrowing(pointcut = "deleteOperation()", throwing = "ex")
    public void handleDataIntegrityViolationException(JoinPoint joinPoint, DataIntegrityViolationException ex) {
        String sourceService = joinPoint.getSignature().getDeclaringTypeName();

        Pattern idPattern = Pattern.compile("\\(id\\)=\\((.*?)\\)");
        Matcher matcher = idPattern.matcher(ex.getMessage());
        String violatingId = "unknown";
        String violatingResource = "unknown";

        if(matcher.find()){
            violatingId = matcher.group(1);
        }

        if(NOAALocationService.class.getName().equals(sourceService)){
            violatingResource = "LOCATION";
        }

        throw new ResourceInUseException(violatingResource,violatingId);
    }
}
