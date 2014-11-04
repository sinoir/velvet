package com.delectable.mobile.api.events.captures;

import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.api.events.BaseEvent;

public class EditedCaptureCommentEvent extends BaseEvent {

    private String mCaptureId;

    public EditedCaptureCommentEvent(boolean isSuccessful, String captureId) {
        super(isSuccessful);
        mCaptureId = captureId;
    }

    public EditedCaptureCommentEvent(String errorMessage, String captureId, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
        mCaptureId = captureId;
    }

    public String getCaptureId() {
        return mCaptureId;
    }
}
