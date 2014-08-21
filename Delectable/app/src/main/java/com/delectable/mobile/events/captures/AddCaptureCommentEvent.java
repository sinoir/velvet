package com.delectable.mobile.events.captures;

import com.delectable.mobile.events.BaseEvent;

public class AddCaptureCommentEvent extends BaseEvent {

    private String mCaptureId;

    public AddCaptureCommentEvent(boolean isSuccessful, String captureId) {
        super(isSuccessful);
        mCaptureId = captureId;
    }

    public AddCaptureCommentEvent(String errorMessage, String captureId) {
        super(errorMessage);
        mCaptureId = captureId;
    }

    public String getCaptureId() {
        return mCaptureId;
    }
}
