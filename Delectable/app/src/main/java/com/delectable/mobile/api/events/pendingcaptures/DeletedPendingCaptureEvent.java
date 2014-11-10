package com.delectable.mobile.api.events.pendingcaptures;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class DeletedPendingCaptureEvent extends BaseEvent {

    public enum State {
        DELETING,
        DELETED,
        FAILED
    }

    private String mRequestId;

    private String mCaptureId;


    private State mState;

    public DeletedPendingCaptureEvent(String requestId, String captureId, State state) {
        super(true);
        mRequestId = requestId;
        mCaptureId = captureId;
        mState = state;
    }

    public DeletedPendingCaptureEvent(String requestId, String errorMessage, String captureId, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
        mRequestId = requestId;
        mCaptureId = captureId;
        mState = State.FAILED;
    }

    public String getCaptureId() {
        return mCaptureId;
    }

    public String getRequestId() {
        return mRequestId;
    }

    public State getState() {
        return mState;
    }
}
