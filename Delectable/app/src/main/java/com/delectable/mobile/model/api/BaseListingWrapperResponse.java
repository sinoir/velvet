package com.delectable.mobile.model.api;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.IDable;

public class BaseListingWrapperResponse<T extends IDable> extends BaseResponse {

    private BaseListingResponse<T> payload;

    public BaseListingResponse<T> getPayload() {
        return payload;
    }

}

