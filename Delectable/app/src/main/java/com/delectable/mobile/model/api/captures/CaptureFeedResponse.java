package com.delectable.mobile.model.api.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.model.api.BaseResponse;

public class CaptureFeedResponse extends BaseResponse {

    private ListingResponse<CaptureDetails> payload;

    public ListingResponse<CaptureDetails> getPayload() {
        return payload;
    }
}
