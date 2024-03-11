package com.example.noaadatamanager.service;

import com.example.noaadatamanager.dtos.input.StationInputDto;
import com.example.noaadatamanager.mapper.StationMapper;
import com.example.noaadatamanager.models.NOAAStation;
import com.example.noaadatamanager.repository.NOAAStationRepository;
import org.springframework.stereotype.Service;

@Service
public class NOAAStationService {
    private final NOAAStationRepository noaaStationRepository;
    private final StationMapper stationMapper;

    public NOAAStationService(NOAAStationRepository noaaStationRepository, StationMapper stationMapper) {
        this.noaaStationRepository = noaaStationRepository;
        this.stationMapper = stationMapper;
    }

    public void create(StationInputDto stationInputDto){
        NOAAStation station = stationMapper.mapToEntity(stationInputDto);
        noaaStationRepository.save(station);
    }
}
