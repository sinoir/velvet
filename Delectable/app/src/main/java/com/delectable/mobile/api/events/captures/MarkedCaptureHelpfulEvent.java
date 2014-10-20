package com.delectable.mobile.api.events.captures;

import com.delectable.mobile.api.events.BaseEvent;

public class MarkedCaptureHelpfulEvent extends BaseEvent {

    private String mCaptureId;

    public MarkedCaptureHelpfulEvent(String captureId, boolean success) {
        super(success);
        mCaptureId = captureId;
    }

    public MarkedCaptureHelpfulEvent(String captureId, String errorMessage) {
        super(errorMessage);
        mCaptureId = captureId;
    }

    public String getCaptureId() {
        return mCaptureId;
    }
}
