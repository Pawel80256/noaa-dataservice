package com.example.noaadatamanager.service;

import com.example.noaadatamanager.dtos.input.MeasurementInputDto;
import com.example.noaadatamanager.dtos.output.MeasurementExtremeValuesDto;
import com.example.noaadatamanager.dtos.output.MeasurementStatisticsDto;
import com.example.noaadatamanager.dtos.update.MeasurementUpdateCommentDto;
import com.example.noaadatamanager.dtos.update.MeasurementUpdateValueDto;
import com.example.noaadatamanager.exceptions.CalculationModuleException;
import com.example.noaadatamanager.exceptions.ValidationException;
import com.example.noaadatamanager.mapper.MeasurementMapper;
import com.example.noaadatamanager.entities.Measurement;
import com.example.noaadatamanager.repository.MeasurementRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

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

    public String create(MeasurementInputDto measurementInputDto){
        Measurement measurement = measurementMapper.mapToEntity(measurementInputDto);
        measurementRepository.save(measurement);
        return measurement.getId();
    }

    public void delete(String measurementId){
        measurementRepository.deleteById(measurementId);
    }

    public String updateValue(MeasurementUpdateValueDto dto) {
        Measurement measurement = measurementRepository.findById(dto.getEntityId()).get();
        measurement.setValue(dto.getUpdatedFieldValue());
        measurementRepository.save(measurement);
        return measurement.getId();
    }

    public String updateComment(MeasurementUpdateCommentDto dto) {
        Measurement measurement = measurementRepository.findById(dto.getEntityId()).get();
        measurement.setComment(dto.getUpdatedFieldValue());
        measurementRepository.save(measurement);
        return measurement.getId();
    }

    public MeasurementStatisticsDto calculateStatistics(List<String> measurementIds){
        List<Measurement> measurements = measurementRepository.findAllById(measurementIds);
        MeasurementStatisticsDto measurementsStatistics;

        try{
            measurementsStatistics = restClient
                    .post()
                    .uri("/measurements/statistics")
                    .body(measurements)
                    .retrieve()
                    .body(MeasurementStatisticsDto.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return measurementsStatistics;
    }

    public Double calculateAverage(List<String> measurementIds) {
        List<Measurement> measurements = measurementRepository.findAllById(measurementIds);
        Double averageValue;

        averageValue = restClient
                .post()
                .uri("/measurements/average")
                .body(measurements)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request,response) -> {
                    throw new CalculationModuleException("Invalid input, check calculation module logs for details");
                 })
                .body(Double.class);

        return averageValue;
    }

    public MeasurementExtremeValuesDto calculateExtremeValues(List<String> measurementIds) {
        List<Measurement> measurements = measurementRepository.findAllById(measurementIds);
        MeasurementExtremeValuesDto extremeValues;

        try{
            extremeValues = restClient
                    .post()
                    .uri("/measurements/extremes")
                    .body(measurements)
                    .retrieve()
                    .body(MeasurementExtremeValuesDto.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return extremeValues;
    }

    public Double calculateStandardDeviation(List<String> measurementIds){
        List<Measurement> measurements = measurementRepository.findAllById(measurementIds);
        Double standardDeviation;

        try{
            standardDeviation = restClient
                    .post()
                    .uri("/measurements/standard-deviation")
                    .body(measurements)
                    .retrieve()
                    .body(Double.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return standardDeviation;
    }

    public Double calculateMedian(List<String> measurementIds){
        List<Measurement> measurements = measurementRepository.findAllById(measurementIds);
        Double median;

        try{
            median = restClient
                    .post()
                    .uri("/measurements/median")
                    .body(measurements)
                    .retrieve()
                    .body(Double.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return median;
    }
}
