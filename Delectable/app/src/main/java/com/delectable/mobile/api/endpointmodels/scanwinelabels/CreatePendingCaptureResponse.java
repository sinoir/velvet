package com.delectable.mobile.api.endpointmodels.scanwinelabels;

import com.delectable.mobile.api.endpointmodels.BaseResponse;
import com.delectable.mobile.api.models.PendingCapture;

public class CreatePendingCaptureResponse extends BaseResponse {

    private Payload payload;

    public PendingCapture getPendingCapture() {
        if (payload == null) {
            return null;
        }

        return payload.pending_capture;
    }

    public static class Payload {

        private PendingCapture pending_capture;
    }
}
