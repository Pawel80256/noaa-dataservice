package com.master.dataloader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.master.dataloader.constant.Constants;
import com.master.dataloader.models.NOAADataType;
import com.master.dataloader.utils.Utils;
import org.springframework.stereotype.Service;

@Service
public class NOAADataTypeService {

    public NOAADataType getById(String id) throws Exception {
        String dataTypesUrl = Constants.baseNoaaApiUrl + Constants.dataTypeUrl + id;
        String requestResult = Utils.sendRequest(dataTypesUrl,null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        JsonNode rootNode = mapper.readTree(requestResult.toString());
        return mapper.readerFor(NOAADataType.class).readValue(rootNode);
    }

}
