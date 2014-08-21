package com.delectable.mobile.events.captures;

import com.delectable.mobile.events.BaseEvent;

public class LikedCaptureEvent extends BaseEvent {

    private String mCaptureId;

    public LikedCaptureEvent(boolean isSuccessful, String captureId) {
        super(isSuccessful);
        mCaptureId = captureId;
    }

    public LikedCaptureEvent(String errorMessage, String captureId) {
        super(errorMessage);
        mCaptureId = captureId;
    }

    public String getCaptureId() {
        return mCaptureId;
    }
}
