package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.models.NOAALocation;
import com.master.dataloader.models.NOAAStation;
import com.master.dataloader.repository.NOAAStationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NOAAStationService {

    private final NOAAStationRepository noaaStationRepository;

    public NOAAStationService(NOAAStationRepository noaaStationRepository) {
        this.noaaStationRepository = noaaStationRepository;
    }

    public NOAAStation getById(String id) throws Exception {
        String stationsUrl = Constants.baseNoaaApiUrl + Constants.stationUrl + id;
        String requestResult = Utils.sendRequest(stationsUrl,null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult);
        return mapper.readerFor(NOAAStation.class).readValue(rootNode);
    }


    public List<NOAAStation> getRemoteByLocationId(String locationId) throws Exception {
        String stationsUrl = Constants.baseNoaaApiUrl + Constants.stationsUrl;
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("locationid",locationId);
        requestParams.put("limit",1000);

        String requestResult = Utils.sendRequest(stationsUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult);
        JsonNode resultsNode = rootNode.path("results");

        return mapper.readerForListOf(NOAAStation.class).readValue(resultsNode);
    }

    public void loadDataTypesAndCategoriesForStations(List<String> stationIds){
        List<NOAAStation> stations = noaaStationRepository.findAllById(stationIds);

        for(NOAAStation station : stations){

        }
    }

    public List<NOAAStation> getAll() {
        return noaaStationRepository.findAll();
    }

    public void loadByIdsAndLocationId(String locationId, List<String> stationIds) throws Exception {
        List<NOAAStation> remoteByLocationId = getRemoteByLocationId(locationId);
        List<NOAAStation> stationsToLoad = remoteByLocationId.stream().filter(station -> stationIds.contains(station.getId())).toList();
        for(NOAAStation station : stationsToLoad){
            station.setNoaaLocation(new NOAALocation(locationId));
        }
        noaaStationRepository.saveAll(stationsToLoad);
    }

    public void deleteByIds(List<String> stationIds) {
        noaaStationRepository.deleteAllById(stationIds);
    }
}
