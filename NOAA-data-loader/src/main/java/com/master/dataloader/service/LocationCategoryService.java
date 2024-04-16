package com.master.dataloader.service;

import com.master.dataloader.constant.URLs;
import com.master.dataloader.dtos.LocationCategoryDto;
import com.master.dataloader.entities.LocationCategory;
import com.master.dataloader.repository.LocationCategoryRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class LocationCategoryService {

    private final LocationCategoryRepository locationCategoryRepository;

    public LocationCategoryService(LocationCategoryRepository locationCategoryRepository) {
        this.locationCategoryRepository = locationCategoryRepository;
    }

    public List<LocationCategoryDto> getAll() {
        return locationCategoryRepository.findAll().stream().map(LocationCategoryDto::new).toList();
    }

    public List<LocationCategoryDto> getAllRemoteDtos() throws Exception {
        List<LocationCategory> remoteLocationCategories = getAllRemote();

        List<LocationCategoryDto> result = remoteLocationCategories.stream().map(LocationCategoryDto::new).toList();

        List<String> localLocationCategoriesIds = locationCategoryRepository.findAllById(
                remoteLocationCategories.stream().map(LocationCategory::getId).toList()
        ).stream().map(LocationCategory::getId).toList();

        result.forEach(lc -> lc.setIsLoaded(localLocationCategoriesIds.contains(lc.getId())));

        return result;
    }

    public void loadByIds(List<String> ids) throws Exception {
        List<LocationCategory> locationCategories =
                    getAllRemote().stream()
                            .filter(locationCategory -> ids.contains(locationCategory.getId()))
                            .toList();

        locationCategoryRepository.saveAll(locationCategories);
    }


    public void deleteByIds(List<String> ids) {
        locationCategoryRepository.deleteAllById(ids);
    }

    private List<LocationCategory> getAllRemote() throws Exception {
        Map<String,Object> requestParams = Utils.getBasicParams();

        String locationCategoriesUrl = URLs.baseNoaaApiUrl + URLs.locationCategoriesUrl;

        List<LocationCategory> result = Utils.getRemoteData(locationCategoriesUrl,requestParams, LocationCategory.class);

        List<String> supportedLocationCategoriesIds = Arrays.asList("CNTRY","CITY","ST");
        List<LocationCategory> supportedLocationCategories = result.stream().filter(lc -> supportedLocationCategoriesIds.contains(lc.getId())).toList();

        return supportedLocationCategories;
    }
}
