package com.delectable.mobile.events.scanwinelabel;

import com.delectable.mobile.api.models.PendingCapture;
import com.delectable.mobile.events.BaseEvent;

public class CreatedPendingCaptureEvent extends BaseEvent {

    private PendingCapture mPendingCapture;

    public CreatedPendingCaptureEvent(PendingCapture pendingCapture) {
        super(true);
        mPendingCapture = pendingCapture;
    }

    public CreatedPendingCaptureEvent(String errorMessage) {
        super(errorMessage);
    }

    public PendingCapture getPendingCapture() {
        return mPendingCapture;
    }
}
