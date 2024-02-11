package com.master.dataloader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.dto.PaginationData;
import com.master.dataloader.dto.PaginationWrapper;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.repository.NOAADataTypeRepository;
import com.master.dataloader.service.NOAADataTypeService;
import com.master.dataloader.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private final NOAADataTypeRepository noaaDataTypeRepository;
    private final NOAADataTypeService noaaDataTypeService;

    public NOAADataTypeController(NOAADataTypeRepository noaaDataTypeRepository, NOAADataTypeService noaaDataTypeService) {
        this.noaaDataTypeRepository = noaaDataTypeRepository;
        this.noaaDataTypeService = noaaDataTypeService;
    }

    @GetMapping
    public ResponseEntity<List<NOAADataType>> getAll(){
        return ResponseEntity.ok(noaaDataTypeService.getAll());
    }

    @GetMapping("/remote")
    public ResponseEntity<List<NOAADataType>> getAllRemote() throws Exception {
        return ResponseEntity.ok(noaaDataTypeService.getAllRemote());
    }

    @PutMapping("/load")
    public ResponseEntity<Void> loadAll() throws Exception {
        noaaDataTypeService.loadAll();
        return ResponseEntity.ok().build();
    }

    @PutMapping("loadByIds")
    public ResponseEntity<Void> loadByIds(@RequestBody List<String> ids) throws Exception {
        noaaDataTypeService.loadByIds(ids);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> ids){
        noaaDataTypeService.deleteByIds(ids);
        return ResponseEntity.ok().build();
    }
}
