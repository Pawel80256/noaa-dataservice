package com.example.noaadatamanager.aspects.validation.createValidation;

import com.example.noaadatamanager.aspects.AspectUtils;
import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.dtos.input.StationInputDto;
import com.example.noaadatamanager.exceptions.ValidationException;
import com.example.noaadatamanager.models.Station;
import com.example.noaadatamanager.repository.MeasurementRepository;
import com.example.noaadatamanager.repository.DataTypeRepository;
import com.example.noaadatamanager.repository.StationRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class CreateMeasurementValidationAspect {

    private MeasurementRepository measurementRepository;
    private DataTypeRepository dataTypeRepository;
    private StationRepository stationRepository;

    public void setNoaaDataRepository(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public void setNoaaDataTypeRepository(DataTypeRepository dataTypeRepository) {
        this.dataTypeRepository = dataTypeRepository;
    }

    public void setNoaaStationRepository(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Pointcut("execution(* com.example.noaadatamanager.service.MeasurementService.create(..))")
    public void createMethod(){}

    @Before("createMethod()")
    public void validateCreateMethod(JoinPoint joinPoint){
        List<Object> methodArguments = List.of(joinPoint.getArgs());

        if(methodArguments.size() != 1 || !(methodArguments.getFirst() instanceof MeasurementInputDto inputDto)){
            throw new ValidationException(
                    AspectUtils.getMethodMetadata(joinPoint) + " should consume only one argument of type "
                            + MeasurementInputDto.class.getSimpleName()
            );
        }

        String measurementId = inputDto.getDataTypeId() + inputDto.getDate().toString() + inputDto.getStationId();
        if(measurementRepository.existsById(measurementId)){
            throw new ValidationException("Measurement with id \"" + measurementId +"\" already exists");
        }

        if(!dataTypeRepository.existsById(inputDto.getDataTypeId())){
            throw new ValidationException("Data type with id \"" + inputDto.getDataTypeId() + "\" does not exist");
        }

        if(!stationRepository.existsById(inputDto.getStationId())){
            throw new ValidationException("Station with id \"" + inputDto.getStationId() + "\" does not exist");
        }

        Station station = stationRepository.findById(inputDto.getStationId()).get();

        if(inputDto.getDate().isAfter(station.getMaxDate()) || inputDto.getDate().isBefore(station.getMinDate())){
            throw new ValidationException("Measurement date for station \"" + inputDto.getStationId() +"\" have to be between " + station.getMinDate() + " - " + station.getMaxDate());
        }
    }


}
