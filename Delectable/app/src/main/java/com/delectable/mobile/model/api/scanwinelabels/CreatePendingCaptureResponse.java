package com.delectable.mobile.model.api.scanwinelabels;

import com.delectable.mobile.api.models.PendingCapture;
import com.delectable.mobile.model.api.BaseResponse;

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
