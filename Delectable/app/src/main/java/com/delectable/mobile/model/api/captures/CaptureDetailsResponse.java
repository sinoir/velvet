package com.delectable.mobile.model.api.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.model.api.BaseResponse;

public class CaptureDetailsResponse extends BaseResponse {

    public CaptureDetailsPayload payload;

    public static class CaptureDetailsPayload {

        public CaptureDetails capture;
    }
}
