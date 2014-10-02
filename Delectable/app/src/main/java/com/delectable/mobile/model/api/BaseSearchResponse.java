package com.delectable.mobile.model.api;

import com.delectable.mobile.api.models.SearchResult;

public class BaseSearchResponse<T> extends BaseResponse {

    private SearchResult<T> payload;

    public SearchResult<T> getPayload() {
        return payload;
    }
}
