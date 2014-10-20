package com.delectable.mobile.api.endpointmodels;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.IDable;

public class BaseListingWrapperResponse<T extends IDable> extends BaseResponse {

    private BaseListingResponse<T> payload;

    public BaseListingResponse<T> getPayload() {
        return payload;
    }

}

