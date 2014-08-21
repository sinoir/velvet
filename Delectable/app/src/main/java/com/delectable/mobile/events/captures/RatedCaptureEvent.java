package com.delectable.mobile.events.captures;

import com.delectable.mobile.events.BaseEvent;

public class RatedCaptureEvent extends BaseEvent {

    private String mCaptureId;

    public RatedCaptureEvent(boolean isSuccessful, String captureId) {
        super(isSuccessful);
        mCaptureId = captureId;
    }

    public RatedCaptureEvent(String errorMessage, String captureId) {
        super(errorMessage);
        mCaptureId = captureId;
    }

    public String getCaptureId() {
        return mCaptureId;
    }
}