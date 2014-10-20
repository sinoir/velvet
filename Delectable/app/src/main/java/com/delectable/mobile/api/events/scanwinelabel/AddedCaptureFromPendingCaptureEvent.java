package com.delectable.mobile.api.events.scanwinelabel;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.util.ErrorUtil;

public class AddedCaptureFromPendingCaptureEvent extends BaseEvent {

    private CaptureDetails mCaptureDetails;

    public AddedCaptureFromPendingCaptureEvent(CaptureDetails captureDetails) {
        super(true);
        mCaptureDetails = captureDetails;
    }

    public AddedCaptureFromPendingCaptureEvent(String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
    }

    public CaptureDetails getCaptureDetails() {
        return mCaptureDetails;
    }
}
