package com.delectable.mobile.api.endpointmodels;

import com.delectable.mobile.api.models.SearchResult;

public class SearchResponse<T> extends BaseResponse {

    private SearchResult<T> payload;

    public SearchResult<T> getPayload() {
        return payload;
    }
}
