package com.master.dataloader.dto;

import java.util.List;

public class PaginationWrapper<T> {
    private Integer offset;
    private Integer count;
    private Integer limit;
    private List<T> data;

    public PaginationWrapper(Integer offset, Integer count, Integer limit, List<T> data) {
        this.offset = offset;
        this.count = count;
        this.limit = limit;
        this.data = data;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getLimit() {
        return limit;
    }

    public List<T> getData() {
        return data;
    }
}
