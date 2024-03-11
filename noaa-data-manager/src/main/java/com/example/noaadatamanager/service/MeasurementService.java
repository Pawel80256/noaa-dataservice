package com.example.noaadatamanager.service;

import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.mapper.MeasurementMapper;
import com.example.noaadatamanager.models.Measurement;
import com.example.noaadatamanager.repository.MeasurementRepository;
import org.springframework.stereotype.Service;

@Service
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
    private final MeasurementMapper measurementMapper;
    public MeasurementService(MeasurementRepository measurementRepository, MeasurementMapper measurementMapper) {
        this.measurementRepository = measurementRepository;
        this.measurementMapper = measurementMapper;
    }

    public String create(MeasurementInputDto measurementInputDto){
        Measurement measurement = measurementMapper.mapToEntity(measurementInputDto);
        measurementRepository.save(measurement);
        return measurement.getId();
    }

    public void delete(String measurementId){
        measurementRepository.deleteById(measurementId);
    }
}
