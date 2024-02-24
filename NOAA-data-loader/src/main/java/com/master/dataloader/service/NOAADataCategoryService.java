package com.master.dataloader.service;

import com.master.dataloader.dtos.NOAADataCategoryDto;
import com.master.dataloader.models.NOAADataCategory;
import com.master.dataloader.repository.NOAADataCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NOAADataCategoryService {
    private final NOAADataCategoryRepository noaaDataCategoryRepository;

    public NOAADataCategoryService(NOAADataCategoryRepository noaaDataCategoryRepository) {
        this.noaaDataCategoryRepository = noaaDataCategoryRepository;
    }

    public List<NOAADataCategoryDto> getAll(){
        return noaaDataCategoryRepository.findAll().stream().map(NOAADataCategoryDto::new).toList();
    }

    public List<NOAADataCategoryDto> getAllRemoteDtos(){
        return null;
    }

    private List<NOAADataCategory> getAllRemote(){
        List<NOAADataCategory> result = new ArrayList<>();
        return null;
    }
}
