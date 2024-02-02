package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.models.NOAAStation;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

@Service
public class NOAAStationService {
    public NOAAStation getById(String id) throws Exception {
        String stationsUrl = Constants.baseNoaaApiUrl + Constants.stationUrl + id;
        String requestResult = Utils.sendRequest(stationsUrl,null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        return mapper.readerFor(NOAAStation.class).readValue(rootNode);
    }
}
