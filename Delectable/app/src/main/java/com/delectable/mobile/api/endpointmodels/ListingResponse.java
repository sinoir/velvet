package com.delectable.mobile.api.endpointmodels;

import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.IDable;

public class ListingResponse<T extends IDable> extends BaseResponse {

    private Listing<T> payload;

    //tells use whether or not to dump the current list
    private boolean invalidate;

    public Listing<T> getPayload() {
        return payload;
    }

    public boolean isInvalidate() {
        return invalidate;
    }

}

