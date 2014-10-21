package com.delectable.mobile.api.endpointmodels.captures;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.CaptureNote;

public class CaptureNotesResponse extends BaseResponse {

    private Listing<CaptureNote> payload;

    public Listing<CaptureNote> getPayload() {
        return payload;
    }
}
