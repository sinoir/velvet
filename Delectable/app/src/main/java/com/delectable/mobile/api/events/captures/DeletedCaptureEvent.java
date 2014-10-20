package com.delectable.mobile.api.events.captures;

import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.api.events.BaseEvent;

public class DeletedCaptureEvent extends BaseEvent {

    private String mCaptureId;

    public DeletedCaptureEvent(boolean isSuccessful, String captureId) {
        super(isSuccessful);
        mCaptureId = captureId;
    }

    public DeletedCaptureEvent(String errorMessage, String captureId, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
        mCaptureId = captureId;
    }

    public String getCaptureId() {
        return mCaptureId;
    }
}
