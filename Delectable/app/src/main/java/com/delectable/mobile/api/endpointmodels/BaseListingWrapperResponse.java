package com.delectable.mobile.api.endpointmodels;

import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.IDable;

public class BaseListingWrapperResponse<T extends IDable> extends BaseResponse {

    private Listing<T> payload;

    public Listing<T> getPayload() {
        return payload;
    }

}

