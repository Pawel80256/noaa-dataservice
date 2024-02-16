package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.models.NOAALocationCategory;
import com.master.dataloader.repository.NOAALocationCategoryRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NOAALocationCategoryService {

    private final NOAALocationCategoryRepository noaaLocationCategoryRepository;

    public NOAALocationCategoryService(NOAALocationCategoryRepository noaaLocationCategoryRepository) {
        this.noaaLocationCategoryRepository = noaaLocationCategoryRepository;
    }

    public List<NOAALocationCategory> getAll() {
        return noaaLocationCategoryRepository.findAll();
    }

    public List<NOAALocationCategory> getAllRemote() throws Exception {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("limit",100);
        requestParams.put("offset",1);

        String locationCategoriesUrl = Constants.baseNoaaApiUrl + Constants.locationCategoriesUrl;
        String requestResult = Utils.sendRequest(locationCategoriesUrl,requestParams);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        JsonNode resultsNode = rootNode.path("results");

        List<NOAALocationCategory> result = mapper.readerForListOf(NOAALocationCategory.class).readValue(resultsNode);

        return result;
    }

    public void loadAll() throws Exception {
        List<NOAALocationCategory> locationCategories = getAllRemote();
        noaaLocationCategoryRepository.saveAll(locationCategories);
    }

    public void loadByIds(List<String> ids, boolean singly) throws Exception {
        List<NOAALocationCategory> locationCategories = new ArrayList<>();
        if(singly){
            for(String locationCategoryId : ids){
                locationCategories.add(getRemoteById(locationCategoryId));
            }
        }else {
            locationCategories =
                    getAllRemote().stream()
                            .filter(locationCategory -> ids.contains(locationCategory.getId()))
                            .toList();
        }

        noaaLocationCategoryRepository.saveAll(locationCategories);
    }

    private NOAALocationCategory getRemoteById(String locationCategoryId) throws Exception {
        String locationCategoryUrl = Constants.baseNoaaApiUrl + Constants.locationCategoriesUrl + "/" + locationCategoryId;
        String requestResult = Utils.sendRequest(locationCategoryUrl,null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult);
        return  mapper.readerFor(NOAALocationCategory.class).readValue(rootNode);
    }

    public void deleteByIds(List<String> ids) {
        noaaLocationCategoryRepository.deleteAllById(ids);
    }
}
