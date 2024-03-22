package com.example.noaadatamanager.aspects.updateValidation;

import com.example.noaadatamanager.dtos.update.UpdateDto;
import com.example.noaadatamanager.exceptions.ValidationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class UpdateParamsValidationAspect {
    @Pointcut("args(com.example.noaadatamanager.dtos.update.UpdateDto)")
    public void updateDtoParameter(){}

    @Pointcut("@annotation(com.example.noaadatamanager.annotations.AllowEmptyValue)")
    public void emptyUpdateValueAllowed() {}

    @Before("updateDtoParameter() && !emptyUpdateValueAllowed()")
    public void validateUpdateMethodParams(JoinPoint joinPoint){
        var args = joinPoint.getArgs();

        if(args.length != 1 && !(args[0] instanceof UpdateDto)){
            throw new ValidationException("Incorrect update method parameters");
        }

        var updateDto = (UpdateDto) args[0];

        if(updateDto.getEntityId() == null || updateDto.getEntityId().toString().isBlank()){
            throw new ValidationException("Entity id cannot be empty");
        }

        if(updateDto.getUpdatedFieldValue() == null || updateDto.getUpdatedFieldValue().toString().isBlank()){
            throw new ValidationException("Updated field value cannot be empty");
        }
    }

    @Before("updateDtoParameter() && emptyUpdateValueAllowed()")
    public void validateUpdateMethodParamsWithAEV(JoinPoint joinPoint){
        var args = joinPoint.getArgs();

        if(args.length != 1 && !(args[0] instanceof UpdateDto)){
            throw new ValidationException("Incorrect update method parameters");
        }

        var updateDto = (UpdateDto) args[0];

        if(updateDto.getEntityId() == null || updateDto.getEntityId().toString().isBlank()){
            throw new ValidationException("Entity id cannot be empty");
        }
    }
}
