package com.example.noaadatamanager.aspects.validation.updateValidation;

import com.example.noaadatamanager.dtos.update.interfaces.UpdateDto;
import com.example.noaadatamanager.exceptions.ValidationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class UpdateParamsValidationAspect {

    @Pointcut("args(com.example.noaadatamanager.dtos.update.interfaces.UpdateDto)")
    public void updateDtoParameter(){}

    @Pointcut("args(com.example.noaadatamanager.dtos.update.interfaces.AllowedEmptyUpdateValueDto)")
    public void emptyUpdateValueAllowed(){}

    @Before("updateDtoParameter() && !emptyUpdateValueAllowed()")
    public void validateUpdateMethodParams(JoinPoint joinPoint){
        var updateDto = tryParseUpdateDto(joinPoint);

        if(updateDto.getEntityId() == null || updateDto.getEntityId().toString().isBlank()){
            throw new ValidationException("Entity id cannot be empty");
        }

        if(updateDto.getUpdatedFieldValue() == null || updateDto.getUpdatedFieldValue().toString().isBlank()){
            throw new ValidationException("Updated field value cannot be empty");
        }
    }

    @Before("updateDtoParameter() && emptyUpdateValueAllowed()")
    public void validateUpdateMethodParamsWithAEV(JoinPoint joinPoint){
        var updateDto = tryParseUpdateDto(joinPoint);

        if(updateDto.getEntityId() == null || updateDto.getEntityId().toString().isBlank()){
            throw new ValidationException("Entity id cannot be empty");
        }
    }

    public UpdateDto tryParseUpdateDto(JoinPoint joinPoint){
        var args = joinPoint.getArgs();

        if(args.length != 1 && !(args[0] instanceof UpdateDto)){
            throw new ValidationException("Incorrect update method parameters");
        }

        return  (UpdateDto) args[0];
    }
}
