package com.master.dataloader.controller;

import com.master.dataloader.dtos.DataTypeDto;
import com.master.dataloader.service.DataTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/NOAA/datatypes")
public class DataTypeController {

    private final DataTypeService dataTypeService;

    public DataTypeController(DataTypeService dataTypeService) {
        this.dataTypeService = dataTypeService;
    }

    @GetMapping
    public ResponseEntity<List<DataTypeDto>> getAll(){
        return ResponseEntity.ok(dataTypeService.getAll());
    }

    @GetMapping("/remote")
    public ResponseEntity<List<DataTypeDto>> getAllRemote() throws Exception {
        return ResponseEntity.ok(dataTypeService.getAllRemoteDtos());
    }


    @PutMapping("loadByIds")
    public ResponseEntity<Void> loadByIds(@RequestBody List<String> ids) throws Exception {
        dataTypeService.loadByIds(ids);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestBody List<String> ids){
        dataTypeService.deleteByIds(ids);
        return ResponseEntity.ok().build();
    }
}
