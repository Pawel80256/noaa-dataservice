package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAALocationCategory;
import com.master.dataloader.repository.NOAALocationCategoryRepository;
import com.master.dataloader.repository.NOAALocationRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NOAALocationCategoryService {

    private final NOAALocationCategoryRepository noaaLocationCategoryRepository;

    public NOAALocationCategoryService(NOAALocationCategoryRepository noaaLocationCategoryRepository) {
        this.noaaLocationCategoryRepository = noaaLocationCategoryRepository;
    }

    public PaginationWrapper<NOAALocationCategory> getAll(Integer limit, Integer offset) throws Exception {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",limit);
        requestParams.put("offset",offset);

        String locationCategoriesUrl = Constants.baseNoaaApiUrl + Constants.locationCategoriesUrl;
        String requestResult = Utils.sendRequest(locationCategoriesUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        JsonNode paginationDataNode = rootNode.path("metadata").path("resultset");
        JsonNode resultsNode = rootNode.path("results");


        PaginationData paginationData = mapper.readerFor(PaginationData.class).readValue(paginationDataNode);
        List<NOAALocationCategory> result = mapper.readerForListOf(NOAALocationCategory.class).readValue(resultsNode);


        return new PaginationWrapper<>(paginationData.getOffset(),paginationData.getCount(),paginationData.getLimit(),result);
    }

    public void loadAll(Integer limit, Integer offset) throws Exception {
        List<NOAALocationCategory> locationCategories = getAll(limit,offset).getData();
        noaaLocationCategoryRepository.saveAll(locationCategories);
    }
}
