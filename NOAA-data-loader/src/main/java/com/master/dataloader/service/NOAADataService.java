package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dtos.NOAADataDto;
import com.master.dataloader.models.NOAAData;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.models.NOAAStation;
import com.master.dataloader.repository.NOAADataRepository;
import com.master.dataloader.repository.NOAAStationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<NOAADataDto> getAllDtos(String datasetId, String dataTypeId,
                                           String locationId, String stationId, LocalDate startDate, LocalDate endDate) throws Exception {
        List<NOAAData> entities = getAllRemote(datasetId,dataTypeId,locationId,stationId,startDate,endDate);
        List<String> localMeasurementsIds = noaaDataRepository.findAllByStationIdAndDateBetween(stationId,startDate,endDate).stream().map(NOAAData::getId).toList();
        List<NOAADataDto> result = entities.stream().map(NOAADataDto::new).toList();
        result.forEach(measurement -> {
            measurement.setLoaded(localMeasurementsIds.contains(measurement.getId()));
        });
        return result;
    }


    public void load(String datasetId, String dataTypeId, String stationId, LocalDate startDate, LocalDate endDate) throws Exception {
        NOAAStation station = noaaStationRepository.findById(stationId).orElseThrow(()-> new RuntimeException("station not found"));

        //todo: handle when there are no records in timeframe for station -> this can be checked by station.getMaxDate & minDate, compare with parameters
        //todo: handle when station does not support provided dataset
        List<NOAAData> dataRecords = getAllRemote(datasetId,dataTypeId,null,stationId,startDate,endDate);

        List<NOAADataType> distinctDataTypesForData = dataRecords.stream().map(NOAAData::getDataType)
                .collect(Collectors.groupingBy(NOAADataType::getId)).values()
                .stream().map(dataTypes -> dataTypes.stream().findFirst().get())
                .collect(Collectors.toList());

        station.setDataTypes(distinctDataTypesForData);

//        if(dataRecords.getCount() <= 1000){
            noaaDataRepository.saveAll(dataRecords);
//        }else {
//            System.out.println("More than 1000 records to load, aborting");
//        }
    }

    private List<NOAAData> getAllRemote(String datasetId, String dataTypeId,
                                        String locationId, String stationId, LocalDate startDate, LocalDate endDate) throws Exception {
        List<NOAAData> result = new ArrayList<>();

        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",1000);
        requestParams.put("offset", 1);
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
        JsonNode rootNode = mapper.readTree(requestResult);

        JsonNode paginationDataNode = rootNode.path("metadata").path("resultset");
        PaginationData paginationData = mapper.readerFor(PaginationData.class).readValue(paginationDataNode);
        JsonNode resultsNode = rootNode.path("results");
        result.addAll(
                (mapper.readerForListOf(NOAAData.class).readValue(resultsNode))//.stream().map(NOAADataDto::new).toList()
        );

        if(paginationData.getCount() > 1000){
            for (int i=1001; i<paginationData.getCount() ; i+=1000){
                requestParams.put("offset",i);
                String additionalRequestResult = Utils.sendRequest(dataUrl,requestParams);
                JsonNode additionalRootNode = mapper.readTree(additionalRequestResult);
                result.addAll(
                        (mapper.readerForListOf(NOAAData.class).readValue(additionalRootNode.path("results")))//.stream().map(NOAADataDto::new).toList()
                );
            }
        }

        result.forEach(measurement -> {
            measurement.setId(measurement.getDataType().getId() + measurement.getDate().toString() + measurement.getStation().getId());
        });
        return result;
    }

}
