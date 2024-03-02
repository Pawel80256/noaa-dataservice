package com.master.dataloader.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class ApiResponse {
    private Integer responseCode;
    private JsonNode responseData;

    public ApiResponse(Integer responseCode, JsonNode responseData) {
        this.responseCode = responseCode;
        this.responseData = responseData;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public JsonNode getResponseData() {
        return responseData;
    }
}
