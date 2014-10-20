package com.delectable.mobile.api.events.captures;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class AddCaptureCommentEvent extends BaseEvent {

    private String mCaptureId;

    public AddCaptureCommentEvent(boolean isSuccessful, String captureId) {
        super(isSuccessful);
        mCaptureId = captureId;
    }

    public AddCaptureCommentEvent(String errorMessage, String captureId, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
        mCaptureId = captureId;
    }

    public String getCaptureId() {
        return mCaptureId;
    }
}
