package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.repository.NOAADataTypeRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NOAADataTypeService {

    private final NOAADataTypeRepository noaaDataTypeRepository;

    public NOAADataTypeService(NOAADataTypeRepository noaaDataTypeRepository) {
        this.noaaDataTypeRepository = noaaDataTypeRepository;
    }

    public PaginationWrapper<NOAADataType> getAll(Integer limit, Integer offset, String datasetId, String locationId, String stationId) throws Exception {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("datasetid",datasetId);
        requestParams.put("locationid",locationId);
        requestParams.put("stationId",stationId);
        requestParams.put("limit",limit);
        requestParams.put("offset",offset);


            String datasetsUrl = Constants.baseNoaaApiUrl + Constants.dataTypesUrl;
            String requestResult = Utils.sendRequest(datasetsUrl,requestParams);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            JsonNode rootNode = mapper.readTree(requestResult.toString());
            JsonNode paginationDataNode = rootNode.path("metadata").path("resultset");
            JsonNode resultsNode = rootNode.path("results");

            PaginationData paginationData = mapper.readerFor(PaginationData.class).readValue(paginationDataNode);
            List<NOAADataType> result = mapper.readerForListOf(NOAADataType.class).readValue(resultsNode);

            return new PaginationWrapper<>(paginationData.getOffset(),paginationData.getCount(),paginationData.getLimit(),result);

    }

//    public NOAADataType getById(String id) throws Exception {
//        String dataTypesUrl = Constants.baseNoaaApiUrl + Constants.dataTypeUrl + id;
//        String requestResult = Utils.sendRequest(dataTypesUrl,null);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//
//        JsonNode rootNode = mapper.readTree(requestResult.toString());
//        return mapper.readerFor(NOAADataType.class).readValue(rootNode);
//    }

    public void loadAll() throws Exception {
        List<NOAADataType> dataTypes = new ArrayList<>();
        dataTypes.addAll(getAll(1000,1,null,null,null).getData());
        dataTypes.addAll(getAll(1000,1001,null,null,null).getData());
        noaaDataTypeRepository.saveAll(dataTypes);
    }


}
