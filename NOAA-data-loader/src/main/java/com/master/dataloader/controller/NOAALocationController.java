package com.master.dataloader.controller;

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
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("NOAA/location")
public class NOAALocationController {

    private final NOAALocationRepository noaaLocationRepository;

    public NOAALocationController(NOAALocationRepository noaaLocationRepository) {
        this.noaaLocationRepository = noaaLocationRepository;
    }

    @GetMapping
    public ResponseEntity<PaginationWrapper<NOAALocation>> getAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
            @RequestParam(name = "datasetId", required = false) String datasetId,
            @RequestParam(name = "dataCategoryId", required = false) String dataCategoryId,
            @RequestParam(name = "locationCategoryId", required = false) String locationCategoryId,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
            ) throws Exception {

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

        return ResponseEntity.ok(
                new PaginationWrapper<>(paginationData.getOffset(),paginationData.getCount(),paginationData.getLimit(),result)
        );
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
            @RequestParam(name = "datasetId", required = false) String datasetId,
            @RequestParam(name = "dataCategoryId", required = false) String dataCategoryId,
            @RequestParam(name = "locationCategoryId", required = false) String locationCategoryId,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) throws Exception {
        List<NOAALocation> locations = getAll(limit,offset,datasetId,dataCategoryId,locationCategoryId,startDate,endDate)
                .getBody().getData();
        noaaLocationRepository.saveAll(locations);
        return ResponseEntity.ok().build();
    }
}
