package com.example.noaadatacalculationmodule.service;

import com.example.noaadatacalculationmodule.models.Measurement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementCalcService {

    public Double calculateAverageValue(List<Measurement> measurements){
        int counter = 0;
        double valueSum = 0;

        for(Measurement measurement : measurements){
            counter ++;
            valueSum += measurement.getValue();
        }

        return (valueSum/counter);
    }

}
