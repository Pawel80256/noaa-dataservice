package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.repository.NOAADataTypeRepository;
import com.master.dataloader.service.NOAADataTypeService;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/NOAA/datatypes")
public class NOAADataTypeController {

    private final NOAADataTypeRepository noaaDataTypeRepository;
    private final NOAADataTypeService noaaDataTypeService;

    public NOAADataTypeController(NOAADataTypeRepository noaaDataTypeRepository, NOAADataTypeService noaaDataTypeService) {
        this.noaaDataTypeRepository = noaaDataTypeRepository;
        this.noaaDataTypeService = noaaDataTypeService;
    }

    @GetMapping
    public PaginationWrapper<NOAADataType> getAll(
            @RequestParam(name = "datasetId", required = false) String datasetId,
            @RequestParam(name = "locationId", required = false) String locationId,
            @RequestParam(name = "stationId", required = false) String stationId,
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ){
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("datasetid",datasetId);
        requestParams.put("locationid",locationId);
        requestParams.put("stationId",stationId);
        requestParams.put("limit",limit);
        requestParams.put("offset",offset);

        try {
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
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{dataTypeId}")
    public ResponseEntity<NOAADataType> getById(@PathVariable("dataTypeId") String dataTypeId) throws Exception {
        return ResponseEntity.ok(noaaDataTypeService.getById(dataTypeId));
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadDataTypes(
            @RequestParam(name = "datasetId", required = false) String datasetId,
            @RequestParam(name = "locationId", required = false) String locationId,
            @RequestParam(name = "stationId", required = false) String stationId,
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ){
        List<NOAADataType> dataTypes = getAll(datasetId,locationId,stationId,limit,offset).getData();
        noaaDataTypeRepository.saveAll(dataTypes);
        return ResponseEntity.ok().build();
    }
}
