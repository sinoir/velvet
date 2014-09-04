package com.delectable.mobile.events.wines;

import com.delectable.mobile.events.BaseEvent;

public class UpdatedBaseWineEvent extends BaseEvent {

    private String mBaseWineId;

    public UpdatedBaseWineEvent(boolean isSuccessful, String baseWineId) {
        super(isSuccessful);
        mBaseWineId = baseWineId;
    }

    public UpdatedBaseWineEvent(String errorMessage, String baseWineId) {
        super(errorMessage);
        mBaseWineId = baseWineId;
    }

    public String getBaseWineId() {
        return mBaseWineId;
    }
}
