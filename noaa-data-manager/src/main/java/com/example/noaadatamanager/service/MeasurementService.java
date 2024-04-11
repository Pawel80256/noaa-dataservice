package com.example.noaadatamanager.service;

import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.dtos.update.MeasurementUpdateCommentDto;
import com.example.noaadatamanager.dtos.update.MeasurementUpdateValueDto;
import com.example.noaadatamanager.mapper.MeasurementMapper;
import com.example.noaadatamanager.models.Measurement;
import com.example.noaadatamanager.repository.MeasurementRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
    private final MeasurementMapper measurementMapper;
    private final RestClient restClient;
    public MeasurementService(MeasurementRepository measurementRepository, MeasurementMapper measurementMapper, RestClient restClient) {
        this.measurementRepository = measurementRepository;
        this.measurementMapper = measurementMapper;
        this.restClient = restClient;
    }

    public Double calculateAverage(List<String> measurementIds){

        List<Measurement> measurements = measurementRepository.findAllById(measurementIds);
        Double averageValue = null;
        try{
            averageValue = restClient
                    .post()
                    .uri("/measurements/average")
                    .body(measurements)
                    .retrieve()
                    .body(Double.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return averageValue;
    }

    public String create(MeasurementInputDto measurementInputDto){
        Measurement measurement = measurementMapper.mapToEntity(measurementInputDto);
        measurementRepository.save(measurement);
        return measurement.getId();
    }

    public void delete(String measurementId){
        measurementRepository.deleteById(measurementId);
    }

    public void updateValue(MeasurementUpdateValueDto dto) {
        Measurement measurement = measurementRepository.findById(dto.getEntityId()).get();
        measurement.setValue(dto.getUpdatedFieldValue());
        measurementRepository.save(measurement);
    }

    public void updateComment(MeasurementUpdateCommentDto dto) {
        Measurement measurement = measurementRepository.findById(dto.getEntityId()).get();
        measurement.setComment(dto.getUpdatedFieldValue());
        measurementRepository.save(measurement);
    }
}
