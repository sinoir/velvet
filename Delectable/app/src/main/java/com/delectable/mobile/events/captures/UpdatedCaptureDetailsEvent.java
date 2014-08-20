package com.delectable.mobile.events.captures;

import com.delectable.mobile.events.BaseEvent;

public class UpdatedCaptureDetailsEvent extends BaseEvent {

    private String mCaptureId;

    public UpdatedCaptureDetailsEvent(boolean isSuccessful, String captureId) {
        super(isSuccessful);
        mCaptureId = captureId;
    }

    public UpdatedCaptureDetailsEvent(String errorMessage, String captureId) {
        super(errorMessage);
        mCaptureId = captureId;
    }

    public String getCaptureId() {
        return mCaptureId;
    }
}
