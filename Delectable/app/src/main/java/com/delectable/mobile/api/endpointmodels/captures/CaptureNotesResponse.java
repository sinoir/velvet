package com.delectable.mobile.api.endpointmodels.captures;

import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.api.endpointmodels.BaseResponse;

public class CaptureNotesResponse extends BaseResponse {

    private ListingResponse<CaptureNote> payload;

    public ListingResponse<CaptureNote> getPayload() {
        return payload;
    }
}
