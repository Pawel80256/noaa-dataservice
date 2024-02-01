package com.master.dataloader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginationData {
    private Integer offset;
    private Integer count;
    private Integer limit;

    public Integer getOffset() {
        return offset;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getLimit() {
        return limit;
    }
}
