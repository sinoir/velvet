package com.delectable.mobile.api.endpointmodels.captures;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetails;

public class CaptureDetailsResponse extends BaseResponse {

    private CaptureDetailsPayload payload;

    public CaptureDetails getCapturePayload() {
        return payload.capture;
    }

    public static class CaptureDetailsPayload {

        private CaptureDetails capture;
    }
}
