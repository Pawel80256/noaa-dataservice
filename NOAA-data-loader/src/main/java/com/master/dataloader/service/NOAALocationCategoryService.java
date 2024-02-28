package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dtos.NoaaLocationCategoryDto;
import com.master.dataloader.models.NOAALocationCategory;
import com.master.dataloader.repository.NOAALocationCategoryRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NOAALocationCategoryService {

    private final NOAALocationCategoryRepository noaaLocationCategoryRepository;

    public NOAALocationCategoryService(NOAALocationCategoryRepository noaaLocationCategoryRepository) {
        this.noaaLocationCategoryRepository = noaaLocationCategoryRepository;
    }

    public List<NoaaLocationCategoryDto> getAll() {
        return noaaLocationCategoryRepository.findAll().stream().map(NoaaLocationCategoryDto::new).toList();
    }

    public List<NoaaLocationCategoryDto> getAllRemoteDtos() throws Exception {
        List<NOAALocationCategory> remoteLocationCategories = getAllRemote();

        List<NoaaLocationCategoryDto> result = remoteLocationCategories.stream().map(NoaaLocationCategoryDto::new).toList();

        List<String> localLocationCategoriesIds = noaaLocationCategoryRepository.findAllById(
                remoteLocationCategories.stream().map(NOAALocationCategory::getId).toList()
        ).stream().map(NOAALocationCategory::getId).toList();

        result.forEach(lc -> lc.setIsLoaded(localLocationCategoriesIds.contains(lc.getId())));

        return result;
    }

    public void loadByIds(List<String> ids) throws Exception {
        List<NOAALocationCategory> locationCategories =
                    getAllRemote().stream()
                            .filter(locationCategory -> ids.contains(locationCategory.getId()))
                            .toList();

        noaaLocationCategoryRepository.saveAll(locationCategories);
    }


    public void deleteByIds(List<String> ids) {
        noaaLocationCategoryRepository.deleteAllById(ids);
    }

    private List<NOAALocationCategory> getAllRemote() throws Exception {
        Map<String,Object> requestParams = Utils.getBasicParams();

        String locationCategoriesUrl = Constants.baseNoaaApiUrl + Constants.locationCategoriesUrl;

        List<NOAALocationCategory> result = Utils.getRemoteData(locationCategoriesUrl,requestParams,NOAALocationCategory.class);

        List<String> supportedLocationCategoriesIds = Arrays.asList("CNTRY","CITY","ST");
        List<NOAALocationCategory> supportedLocationCategories = result.stream().filter(lc -> supportedLocationCategoriesIds.contains(lc.getId())).toList();

        return supportedLocationCategories;
    }
}
