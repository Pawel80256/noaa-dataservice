package com.master.dataloader.aspects;

import com.master.dataloader.constant.Resources;
import com.master.dataloader.exceptions.MissingRelatedResourceException;
import com.master.dataloader.exceptions.ResourceInUseException;
import com.master.dataloader.service.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
public class ExceptionHandlingAspect {
    private final Logger log = LoggerFactory.getLogger(ExceptionHandlingAspect.class);

    @Pointcut("execution(* com.master.dataloader.service.*.delete*(..))")
    public void deleteOperation() {}

    @AfterThrowing(pointcut = "deleteOperation()", throwing = "ex")
    public void handleDataIntegrityViolationException(JoinPoint joinPoint, DataIntegrityViolationException ex) {
        String sourceServiceName = joinPoint.getSignature().getDeclaringTypeName();

        Pattern idPattern = Pattern.compile("\\(id\\)=\\((.*?)\\)");
        Matcher matcher = idPattern.matcher(ex.getMessage());

        String violatingId = Resources.UNKNOWN;

        if(matcher.find()){
            violatingId = matcher.group(1);
        }

        ResourceInUseException resourceInUseException =
                new ResourceInUseException(detectResource(sourceServiceName),violatingId);

        log.error(resourceInUseException.getMessage(),resourceInUseException);

        throw resourceInUseException;
    }

    @Pointcut("execution(* com.master.dataloader.service.*.load*(..))")
    public void loadOperation() {}

    @AfterThrowing(pointcut = "loadOperation()", throwing = "ex")
    public void handleJpaObjectRetrievalFailureException(JoinPoint joinPoint, JpaObjectRetrievalFailureException ex){
        MissingRelatedResourceException missingRelatedResourceException =
                new MissingRelatedResourceException(ex.getMessage());

        log.error(missingRelatedResourceException.getMessage(),missingRelatedResourceException);

        throw missingRelatedResourceException;
    }

    private String detectResource(String sourceServiceName){
        if(sourceServiceName != null && !sourceServiceName.isBlank()) {
            if (LocationService.class.getName().equals(sourceServiceName)) {
                return Resources.LOCATION;
            }
            if (StationService.class.getName().equals(sourceServiceName)) {
                return  Resources.STATION;
            }
            if (DataTypeService.class.getName().equals(sourceServiceName)) {
                return  Resources.DATA_TYPE;
            }
            if (DataCategoryService.class.getName().equals(sourceServiceName)) {
                return  Resources.DATA_CATEGORY;
            }
            if (DatasetService.class.getName().equals(sourceServiceName)) {
                return  Resources.DATASET;
            }
        }
        return Resources.UNKNOWN;
    }

}
