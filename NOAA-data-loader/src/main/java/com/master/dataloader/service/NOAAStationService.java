package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataType;
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

    public PaginationWrapper<NOAAStation> getAll(Integer limit, Integer offset, String locationId, String dataCategoryId, String dataTypeId, LocalDate startDate, LocalDate endDate) throws Exception {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",limit);
        requestParams.put("offset",offset);
        requestParams.put("locationid", locationId);
        requestParams.put("datacategoryid", dataCategoryId);
        requestParams.put("datatypeid", dataTypeId);
        requestParams.put("startdate", startDate);
        requestParams.put("enddate", endDate);

        String stationsUrl = Constants.baseNoaaApiUrl + Constants.stationsUrl;
        String requestResult = Utils.sendRequest(stationsUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        JsonNode paginationDataNode = rootNode.path("metadata").path("resultset");
        JsonNode resultsNode = rootNode.path("results");

        PaginationData paginationData = mapper.readerFor(PaginationData.class).readValue(paginationDataNode);
        List<NOAAStation> result = mapper.readerForListOf(NOAAStation.class).readValue(resultsNode);

        return new PaginationWrapper<>(paginationData.getOffset(),paginationData.getCount(),paginationData.getLimit(),result);
    }

    public NOAAStation getById(String id) throws Exception {
        String stationsUrl = Constants.baseNoaaApiUrl + Constants.stationUrl + id;
        String requestResult = Utils.sendRequest(stationsUrl,null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        return mapper.readerFor(NOAAStation.class).readValue(rootNode);
    }

    public void loadAll(Integer limit, Integer offset, String locationId, String dataCategoryId, String dataTypeId, LocalDate startDate, LocalDate endDate) throws Exception {
        List<NOAAStation> stations = getAll(limit,offset,locationId,dataCategoryId,dataTypeId,startDate,endDate).getData();
        noaaStationRepository.saveAll(stations);
    }

    public NOAAStation getByLocationId(String locationId) throws Exception {
        String stationsUrl = Constants.baseNoaaApiUrl + Constants.stationsUrl;
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("locationid",locationId);

        //todo: check if it can return more than one station for locationId
        String requestResult = Utils.sendRequest(stationsUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        List<NOAAStation> stations = mapper.readerForListOf(NOAAStation.class).readValue(rootNode.path("results"));

        return stations.getFirst();
    }


    //todo: endpoint for loading stations where locationId in (List<String> locationIds)
    public void loadByLocationId(String locationId) throws Exception {
        String stationsUrl = Constants.baseNoaaApiUrl + Constants.stationsUrl;
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("locationid",locationId);

        String requestResult = Utils.sendRequest(stationsUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        List<NOAAStation> stations = mapper.readerForListOf(NOAAStation.class).readValue(rootNode.path("results"));

        noaaStationRepository.save(stations.getFirst());
    }
}
