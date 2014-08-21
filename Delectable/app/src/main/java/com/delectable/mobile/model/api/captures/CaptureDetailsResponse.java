package com.delectable.mobile.model.api.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.model.api.BaseResponse;

public class CaptureDetailsResponse extends BaseResponse {

    private CaptureDetailsPayload payload;

    public CaptureDetails getCapturePayload() {
        return payload.capture;
    }

    public static class CaptureDetailsPayload {

        private CaptureDetails capture;
    }
}
