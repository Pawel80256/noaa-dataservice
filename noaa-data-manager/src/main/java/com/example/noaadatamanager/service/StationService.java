package com.example.noaadatamanager.service;

import com.example.noaadatamanager.dtos.input.StationInputDto;
import com.example.noaadatamanager.mapper.StationMapper;
import com.example.noaadatamanager.models.NOAAStation;
import com.example.noaadatamanager.repository.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class StationService {
    private final StationRepository stationRepository;
    private final StationMapper stationMapper;

    public StationService(StationRepository stationRepository, StationMapper stationMapper) {
        this.stationRepository = stationRepository;
        this.stationMapper = stationMapper;
    }

    public String create(StationInputDto stationInputDto){
        NOAAStation station = stationMapper.mapToEntity(stationInputDto);
        stationRepository.save(station);
        return station.getId();
    }

    public void delete(String stationId){
        stationRepository.deleteById(stationId);
    }
}
