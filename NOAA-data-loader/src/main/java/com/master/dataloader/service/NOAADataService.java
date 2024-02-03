package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAAData;
import com.master.dataloader.models.NOAAStation;
import com.master.dataloader.repository.NOAADataRepository;
import com.master.dataloader.repository.NOAAStationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NOAADataService {

    private final NOAAStationService noaaStationService;
    private final NOAADataRepository noaaDataRepository;
    private final NOAAStationRepository noaaStationRepository;

    public NOAADataService(NOAAStationService noaaStationService, NOAADataRepository noaaDataRepository, NOAAStationRepository noaaStationRepository) {
        this.noaaStationService = noaaStationService;
        this.noaaDataRepository = noaaDataRepository;
        this.noaaStationRepository = noaaStationRepository;
    }

    public PaginationWrapper<NOAAData> getAll(Integer limit, Integer offset, String datasetId, String dataTypeId,
                                              String locationId, String stationId, LocalDate startDate, LocalDate endDate) throws Exception {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",limit);
        requestParams.put("offset", offset);
        requestParams.put("datasetid", datasetId);
        requestParams.put("datatypeid", dataTypeId);
        requestParams.put("locationid", locationId);
        requestParams.put("stationid", stationId);
        requestParams.put("startdate", startDate);
        requestParams.put("enddate", endDate);

        String dataUrl = Constants.baseNoaaApiUrl + Constants.dataUrl;
        String requestResult = Utils.sendRequest(dataUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JsonNode rootNode = mapper.readTree(requestResult.toString());

        JsonNode paginationDataNode = rootNode.path("metadata").path("resultset");
        JsonNode resultsNode = rootNode.path("results");

        PaginationData paginationData = mapper.readerFor(PaginationData.class).readValue(paginationDataNode);
        List<NOAAData> result = mapper.readerForListOf(NOAAData.class).readValue(resultsNode);

        return new PaginationWrapper<>(paginationData.getOffset(),paginationData.getCount(),paginationData.getLimit(),result);
    }

    public void loadAll(Integer limit, Integer offset, String datasetId, String dataTypeId,
                        String locationId, String stationId, LocalDate startDate, LocalDate endDate) throws Exception {
        List<NOAAData> data = getAll(limit,offset,datasetId,dataTypeId,locationId,stationId,startDate,endDate).getData();
        for(NOAAData noaaData : data){
            NOAAStation station = noaaData.getStation();
            if(station != null && !noaaStationRepository.existsById(station.getId())){
                NOAAStation stationFromApi = noaaStationService.getById(station.getId());
                noaaStationRepository.save(stationFromApi);
                noaaData.setStation(station);
            }
        }
        noaaDataRepository.saveAll(data);
    }

    public void loadByStationId(String stationId, LocalDate startDate, LocalDate endDate, String datasetId, String dataTypeId) throws Exception {
        PaginationWrapper<NOAAData> dataRecords = getAll(1000,1,datasetId,dataTypeId,null,stationId,startDate,endDate);
        if(dataRecords.getCount() <= 1000){
            noaaDataRepository.saveAll(dataRecords.getData());
        }else {
            System.out.println("More than 1000 records to load, aborting");
        }
    }

}
