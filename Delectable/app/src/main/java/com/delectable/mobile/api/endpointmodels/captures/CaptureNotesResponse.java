package com.delectable.mobile.api.endpointmodels.captures;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureNote;

public class CaptureNotesResponse extends BaseResponse {

    private BaseListingResponse<CaptureNote> payload;

    public BaseListingResponse<CaptureNote> getPayload() {
        return payload;
    }
}
