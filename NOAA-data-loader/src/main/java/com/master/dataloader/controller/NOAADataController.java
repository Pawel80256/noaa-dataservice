package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAAData;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.models.NOAADataset;
import com.master.dataloader.models.NOAAStation;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("NOAA/data")
public class NOAADataController {

    @GetMapping
    public ResponseEntity<PaginationWrapper<NOAAData>> getAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset,
            @RequestParam(name = "datasetId", required = true) String datasetId,
            @RequestParam(name = "dataTypeId", required = false) String datTypeId,
            @RequestParam(name = "locationId", required = false) String locationId,
            @RequestParam(name = "stationId", required = false) String stationId,
            @RequestParam(name = "startDate", required = true) LocalDate startDate,
            @RequestParam(name = "endDate", required = true) LocalDate endDate
    ) throws Exception {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",limit);
        requestParams.put("offset", offset);
        requestParams.put("datasetid", datasetId);
        requestParams.put("datatypeid", datTypeId);
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

        return ResponseEntity.ok(
                new PaginationWrapper<>(paginationData.getOffset(),paginationData.getCount(),paginationData.getLimit(),result)
        );
    }
}
