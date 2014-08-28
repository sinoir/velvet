package com.delectable.mobile.events.scanwinelabel;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.events.BaseEvent;

public class AddedCaptureFromPendingCaptureEvent extends BaseEvent {

    private CaptureDetails mCaptureDetails;

    public AddedCaptureFromPendingCaptureEvent(CaptureDetails captureDetails) {
        super(true);
        mCaptureDetails = captureDetails;
    }

    public AddedCaptureFromPendingCaptureEvent(String errorMessage) {
        super(errorMessage);
    }

    public CaptureDetails getCaptureDetails() {
        return mCaptureDetails;
    }
}
