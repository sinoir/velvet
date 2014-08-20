package com.delectable.mobile.model.api.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.model.api.BaseResponse;

public class CaptureFeedResponse extends BaseResponse {

    public ListingResponse<CaptureDetails> payload;
}
