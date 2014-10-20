package com.delectable.mobile.api.endpointmodels.captures;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;

//TODO delete this class when tests have been fixed
public class CaptureFeedResponse extends BaseResponse {

    private ListingResponse<CaptureDetails> payload;

    public ListingResponse<CaptureDetails> getPayload() {
        return payload;
    }
}
