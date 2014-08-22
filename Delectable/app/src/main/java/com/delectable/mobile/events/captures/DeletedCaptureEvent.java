package com.delectable.mobile.events.captures;

import com.delectable.mobile.events.BaseEvent;

public class DeletedCaptureEvent extends BaseEvent {

    private String mCaptureId;

    public DeletedCaptureEvent(boolean isSuccessful, String captureId) {
        super(isSuccessful);
        mCaptureId = captureId;
    }

    public DeletedCaptureEvent(String errorMessage, String captureId) {
        super(errorMessage);
        mCaptureId = captureId;
    }

    public String getCaptureId() {
        return mCaptureId;
    }
}
