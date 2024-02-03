package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataset;
import com.master.dataloader.models.NOAALocationCategory;
import com.master.dataloader.repository.NOAALocationCategoryRepository;
import com.master.dataloader.service.NOAALocationCategoryService;
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

    private final NOAALocationCategoryService noaaLocationCategoryService;

    public NOAALocationCategoryController(NOAALocationCategoryService noaaLocationCategoryService) {
        this.noaaLocationCategoryService = noaaLocationCategoryService;
    }

    @GetMapping
    public ResponseEntity<PaginationWrapper<NOAALocationCategory>> getAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ) throws Exception {

        return ResponseEntity.ok(
                noaaLocationCategoryService.getAll(limit,offset)
        );
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadAll(
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "offset", defaultValue = "1") Integer offset
    ) throws Exception {
        noaaLocationCategoryService.loadAll(limit,offset);
        return ResponseEntity.ok().build();
    }

}
