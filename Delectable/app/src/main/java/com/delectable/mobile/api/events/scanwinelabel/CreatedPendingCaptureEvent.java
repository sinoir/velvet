package com.delectable.mobile.api.events.scanwinelabel;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.PendingCapture;
import com.delectable.mobile.api.util.ErrorUtil;

public class CreatedPendingCaptureEvent extends BaseEvent {

    private PendingCapture mPendingCapture;

    public CreatedPendingCaptureEvent(PendingCapture pendingCapture) {
        super(true);
        mPendingCapture = pendingCapture;
    }

    public CreatedPendingCaptureEvent(String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
    }

    public PendingCapture getPendingCapture() {
        return mPendingCapture;
    }
}
