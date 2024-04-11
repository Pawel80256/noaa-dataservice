package com.example.noaadatacalculationmodule.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("measurements")
public class MeasurementCalcController {

    @GetMapping
    public ResponseEntity<String> testEndpoint(){
        return ResponseEntity.ok("reached calculation module");
    }
}
