package com.master.dataloader.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAALocation;
import com.master.dataloader.models.NOAALocationCategory;
import com.master.dataloader.repository.NOAALocationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDate;
import java.util.*;

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
    //najpierw ładuje kraje potem miasta, z miast pobieram kraj do ktorego nalezy


    public void loadAllCountries() throws Exception {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",1000);
        requestParams.put("offset",1);
        requestParams.put("locationcategoryid", "CNTRY");

        String locationsUrl = Constants.baseNoaaApiUrl + Constants.locationsUrl;
        String requestResult = Utils.sendRequest(locationsUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        JsonNode resultsNode = rootNode.path("results");

        List<NOAALocation> locations = mapper.readerForListOf(NOAALocation.class).readValue(resultsNode);

        //check what is faster - stream.foreach() or for(X x :xs)

        for(NOAALocation location : locations){
            location.setNoaaLocationCategory(new NOAALocationCategory("CNTRY"));
        }

        noaaLocationRepository.saveAll(locations);
    }

    public void loadAllStates()throws Exception {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",51);
        requestParams.put("offset",1);
        requestParams.put("locationcategoryid", "ST");

        String locationsUrl = Constants.baseNoaaApiUrl + Constants.locationsUrl;
        String requestResult = Utils.sendRequest(locationsUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        JsonNode resultsNode = rootNode.path("results");

        List<NOAALocation> locations = mapper.readerForListOf(NOAALocation.class).readValue(resultsNode);

        //check what is faster - stream.foreach() or for(X x :xs)
        for(NOAALocation location : locations){
            location.setNoaaLocationCategory(new NOAALocationCategory("ST"));
            location.setParent(new NOAALocation("FIPS:US"));
        }

        noaaLocationRepository.saveAll(locations);
    }

    public void loadAllCities() throws Exception {
        List<NOAALocation> locations = new ArrayList<>();
        String requestResult;
        JsonNode rootNode, resultsNode;

        //1st part
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",1000);
        requestParams.put("offset",1);
        requestParams.put("locationcategoryid", "CITY");

        String locationsUrl = Constants.baseNoaaApiUrl + Constants.locationsUrl;
        requestResult = Utils.sendRequest(locationsUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        rootNode = mapper.readTree(requestResult.toString());
        resultsNode = rootNode.path("results");

        locations.addAll(mapper.readerForListOf(NOAALocation.class).readValue(resultsNode));

        //2nd part
        requestParams.put("offset",1001);
        requestResult = Utils.sendRequest(locationsUrl,requestParams);
        rootNode = mapper.readTree(requestResult.toString());
        resultsNode = rootNode.path("results");
        locations.addAll(mapper.readerForListOf(NOAALocation.class).readValue(resultsNode));

            Iterator<NOAALocation> iterator = locations.iterator();
            while(iterator.hasNext()){
                NOAALocation location = iterator.next();
                String parentCountryId = "FIPS:" + location.getName().substring(location.getName().length()-2);
                if(noaaLocationRepository.existsById(parentCountryId)){
                    location.setNoaaLocationCategory(new NOAALocationCategory("CITY"));
                    location.setParent(new NOAALocation(parentCountryId));
                } else {
                    iterator.remove();
                }
            }


        noaaLocationRepository.saveAll(locations);
    }



}
