package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAALocation;
import com.master.dataloader.repository.NOAALocationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NOAALocationService {
    private final NOAALocationRepository noaaLocationRepository;

    public NOAALocationService(NOAALocationRepository noaaLocationRepository) {
        this.noaaLocationRepository = noaaLocationRepository;
    }

    public PaginationWrapper<NOAALocation> getAll(Integer limit, Integer offset, String datasetId, String dataCategoryId, String locationCategoryId, LocalDate startDate, LocalDate endDate) throws Exception {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",limit);
        requestParams.put("offset",offset);
        requestParams.put("datasetid", datasetId);
        requestParams.put("datacategoryid", dataCategoryId);
        requestParams.put("locationcategoryid", locationCategoryId);
        requestParams.put("startdate", startDate);
        requestParams.put("enddate", endDate);

        String locationsUrl = Constants.baseNoaaApiUrl + Constants.locationsUrl;
        String requestResult = Utils.sendRequest(locationsUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        JsonNode paginationDataNode = rootNode.path("metadata").path("resultset");
        JsonNode resultsNode = rootNode.path("results");


        PaginationData paginationData = mapper.readerFor(PaginationData.class).readValue(paginationDataNode);
        List<NOAALocation> result = mapper.readerForListOf(NOAALocation.class).readValue(resultsNode);


        return new PaginationWrapper<>(paginationData.getOffset(),paginationData.getCount(),paginationData.getLimit(),result);
    }

    public void loadAll(Integer limit, Integer offset, String datasetId, String dataCategoryId, String locationCategoryId, LocalDate startDate, LocalDate endDate) throws Exception {
        List<NOAALocation> locations = getAll(limit,offset,datasetId,dataCategoryId,locationCategoryId,startDate,endDate).getData();
        noaaLocationRepository.saveAll(locations);
    }

}
