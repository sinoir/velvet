package com.delectable.mobile.api.endpointmodels.captures;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetails;

public class CaptureDetailsResponse extends BaseResponse {

    private Payload payload;

    public CaptureDetails getCapture() {
        if (payload == null) {
            return null;
        }

        return payload.capture;
    }

    public static class Payload {

        private CaptureDetails capture;
    }
}
