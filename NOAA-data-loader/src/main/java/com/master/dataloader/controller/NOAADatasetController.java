package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataset;
import com.master.dataloader.repository.NOAADatasetRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("NOAA/datasets")
public class NOAADatasetController {

    private final NOAADatasetRepository noaaDatasetRepository;

    public NOAADatasetController(NOAADatasetRepository noaaDatasetRepository) {
        this.noaaDatasetRepository = noaaDatasetRepository;
    }

    @GetMapping()
    public PaginationWrapper<NOAADataset> getAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ) throws Exception {

        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",limit);
        requestParams.put("offset",offset);

        String dataTypesUrl = Constants.baseNoaaApiUrl + Constants.datasetsUrl;
        URL url = new URL(
                Utils.buildUrlWithParams(dataTypesUrl,requestParams)
        );
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
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
        List<NOAADataset> result = mapper.readerForListOf(NOAADataset.class).readValue(resultsNode);

        return new PaginationWrapper<>(paginationData.getOffset(),paginationData.getCount(),paginationData.getLimit(),result);
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadAllDatasets(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ) throws Exception {
        List<NOAADataset> datasets = getAll(limit,offset).getData();
        noaaDatasetRepository.saveAll(datasets);
        return ResponseEntity.ok().build();
    }

}
