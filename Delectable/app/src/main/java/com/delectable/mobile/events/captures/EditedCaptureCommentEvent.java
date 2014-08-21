package com.delectable.mobile.events.captures;

import com.delectable.mobile.events.BaseEvent;

public class EditedCaptureCommentEvent extends BaseEvent {

    private String mCaptureId;

    public EditedCaptureCommentEvent(boolean isSuccessful, String captureId) {
        super(isSuccessful);
        mCaptureId = captureId;
    }

    public EditedCaptureCommentEvent(String errorMessage, String captureId) {
        super(errorMessage);
        mCaptureId = captureId;
    }

    public String getCaptureId() {
        return mCaptureId;
    }
}
