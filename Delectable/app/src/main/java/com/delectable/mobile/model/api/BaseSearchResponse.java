package com.delectable.mobile.model.api;

import com.delectable.mobile.api.models.SearchHit;
import com.delectable.mobile.api.models.SearchResult;
import com.delectable.mobile.model.api.BaseResponse;

import java.util.ArrayList;

public class BaseSearchResponse<T> extends BaseResponse {

    private SearchResult<T> payload;

    public SearchResult<T> getPayload() {
        return payload;
    }
}
