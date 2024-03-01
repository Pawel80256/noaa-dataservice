package com.master.dataloader.service;

import com.master.dataloader.constant.Constants;
import com.master.dataloader.dtos.NOAADataCategoryDto;
import com.master.dataloader.models.NOAADataCategory;
import com.master.dataloader.repository.NOAADataCategoryRepository;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NOAADataCategoryService {
    private final NOAADataCategoryRepository noaaDataCategoryRepository;

    public NOAADataCategoryService(NOAADataCategoryRepository noaaDataCategoryRepository) {
        this.noaaDataCategoryRepository = noaaDataCategoryRepository;
    }

    public List<NOAADataCategoryDto> getAll(){
        return noaaDataCategoryRepository.findAll().stream().map(NOAADataCategoryDto::new).toList();
    }

    public List<NOAADataCategoryDto> getAllRemoteDtos() throws Exception {
        List<NOAADataCategory> remoteDataCategories = getAllRemote();

        List<NOAADataCategoryDto> result = remoteDataCategories.stream().map(NOAADataCategoryDto::new).toList();

        List<String> localDataCategoriesIds = noaaDataCategoryRepository.findAllById(
                remoteDataCategories.stream().map(NOAADataCategory::getId).toList()
        ).stream().map(NOAADataCategory::getId).toList();

        result.forEach(dc -> dc.setLoaded(localDataCategoriesIds.contains(dc.getId())));

        return result;
    }

    public void loadByIds(List<String> dataCategoriesIds) throws Exception {
        List<NOAADataCategory> dataCategoriesToLoad = getAllRemote().stream()
                .filter(dc -> dataCategoriesIds.contains(dc.getId()))
                .toList();

        noaaDataCategoryRepository.saveAll(dataCategoriesToLoad);
    }

    public void deleteByIds(List<String> ids){
        noaaDataCategoryRepository.deleteAllById(ids);
    }

    private List<NOAADataCategory> getAllRemote() throws Exception {
        Map<String, Object> requestParams = Utils.getBasicParams();

        String datasetsUrl = Constants.baseNoaaApiUrl + Constants.dataCategoriesUrl;

        return Utils.getRemoteData(datasetsUrl,requestParams, NOAADataCategory.class);
    }
}
