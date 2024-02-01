package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.utils.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public PaginationWrapper<NOAADataType> getDataTypes(
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
            URL url  = new URL(
                    Utils.buildUrlWithParams(datasetsUrl,requestParams)
            );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            Utils.addTokenHeader(connection);

            StringBuilder requestResult = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestResult.append(line);
                }
            }

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
}