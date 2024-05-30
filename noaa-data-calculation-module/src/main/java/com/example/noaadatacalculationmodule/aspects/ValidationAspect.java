package com.example.noaadatacalculationmodule.aspects;

import com.example.noaadatacalculationmodule.exceptions.ValidationException;
import com.example.noaadatacalculationmodule.models.Measurement;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Aspect
public class ValidationAspect {
    private final Logger log = LoggerFactory.getLogger(ValidationAspect.class);

    @Pointcut("execution(public * com.example.noaadatacalculationmodule.service.MeasurementCalcService.calculateAverageValue(..))")
    public void average(){}

    @Pointcut("execution(public * com.example.noaadatacalculationmodule.service.MeasurementCalcService.calculateExtremeValues(..))")
    public void extremeValues(){}

    @Pointcut("execution(public * com.example.noaadatacalculationmodule.service.MeasurementCalcService.calculateStandardDeviation(..))")
    public void standardDeviation(){}

    @Pointcut("execution(public * com.example.noaadatacalculationmodule.service.MeasurementCalcService.calculateMedian(..))")
    public void median(){}

    //walidacja na potrzeby testów wydajnościowych
    @Before("average()")
    public void validateAverage(JoinPoint joinPoint){
        List<Measurement> measurements = (List<Measurement>) joinPoint.getArgs()[0];
        if(measurements.size() < 2){
            String message = "At least 2 measurements required";
            log.error(message);
            throw new ValidationException(message);
        }

        if(measurements.size() > 10){
            String message = "Max 10 measurements allowed";
            log.error(message);
            throw new ValidationException(message);
        }
    }

    @Before("average()")
    public void validateExtremeValues(JoinPoint joinPoint){
        List<Measurement> measurements = (List<Measurement>) joinPoint.getArgs()[0];
        if(measurements.size() < 2){
            String message = "At least 2 measurements required";
            log.error(message);
            throw new ValidationException(message);
        }

        if(measurements.size() > 10){
            String message = "Max 10 measurements allowed";
            log.error(message);
            throw new ValidationException(message);
        }
    }

    @Before("standardDeviation()")
    public void validateStandardDeviation(JoinPoint joinPoint){
        List<Measurement> measurements = (List<Measurement>) joinPoint.getArgs()[0];
        if(measurements.size() < 2){
            String message = "At least 2 measurements required";
            log.error(message);
            throw new ValidationException(message);
        }

        if(measurements.size() > 10){
            String message = "Max 10 measurements allowed";
            log.error(message);
            throw new ValidationException(message);
        }
    }

    @Before("median()")
    public void validateMedian(JoinPoint joinPoint){
        List<Measurement> measurements = (List<Measurement>) joinPoint.getArgs()[0];
        if(measurements.size() < 2){
            String message = "At least 2 measurements required";
            log.error(message);
            throw new ValidationException(message);
        }

        if(measurements.size() > 10){
            String message = "Max 10 measurements allowed";
            log.error(message);
            throw new ValidationException(message);
        }
    }
}
