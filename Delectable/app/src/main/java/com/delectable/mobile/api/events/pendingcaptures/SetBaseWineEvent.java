package com.delectable.mobile.api.events.pendingcaptures;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class SetBaseWineEvent extends BaseEvent {

    private String mPendingCaptureId;

    private String mBaseWineId;

    public SetBaseWineEvent(String pendingCaptureId, String baseWineId) {
        super(true);
        mPendingCaptureId = pendingCaptureId;
        mBaseWineId = baseWineId;
    }

    public SetBaseWineEvent(String pendingCaptureId, String baseWineId, String errorMessage,
            ErrorUtil errorCode) {
        super(errorMessage, errorCode);
        mPendingCaptureId = pendingCaptureId;
        mBaseWineId = baseWineId;
    }

    public String getPendingCaptureId() {
        return mPendingCaptureId;
    }

    public String getBaseWineId() {
        return mBaseWineId;
    }
}
