package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataset;
import com.master.dataloader.models.NOAALocationCategory;
import com.master.dataloader.repository.NOAALocationCategoryRepository;
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
@RequestMapping("NOAA/location-category")
public class NOAALocationCategoryController {

    private final NOAALocationCategoryRepository noaaLocationCategoryRepository;

    public NOAALocationCategoryController(NOAALocationCategoryRepository noaaLocationCategoryRepository) {
        this.noaaLocationCategoryRepository = noaaLocationCategoryRepository;
    }

    @GetMapping
    public ResponseEntity<PaginationWrapper<NOAALocationCategory>> getAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ) throws Exception {

        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",limit);
        requestParams.put("offset",offset);
        String locationCategoriesUrl = Constants.baseNoaaApiUrl + Constants.locationCategoriesUrl;

        URL url = new URL(
                Utils.buildUrlWithParams(locationCategoriesUrl,requestParams)
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

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        JsonNode paginationDataNode = rootNode.path("metadata").path("resultset");
        JsonNode resultsNode = rootNode.path("results");


        PaginationData paginationData = mapper.readerFor(PaginationData.class).readValue(paginationDataNode);
        List<NOAALocationCategory> result = mapper.readerForListOf(NOAALocationCategory.class).readValue(resultsNode);

        return ResponseEntity.ok(
                new PaginationWrapper<>(paginationData.getOffset(),paginationData.getCount(),paginationData.getLimit(),result)
        );
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ) throws Exception {
        List<NOAALocationCategory> locationCategories = getAll(limit,offset).getBody().getData();
        noaaLocationCategoryRepository.saveAll(locationCategories);
        return ResponseEntity.ok().build();
    }

}
