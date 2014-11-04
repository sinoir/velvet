package com.delectable.mobile.api.events.builddatecheck;

import com.delectable.mobile.api.events.BaseEvent;

public class BuildDateCheckedEvent extends BaseEvent {

    private boolean mShouldUpdate;

    public BuildDateCheckedEvent(boolean shouldUpdate) {
        super(true);
        mShouldUpdate = shouldUpdate;
    }

    public BuildDateCheckedEvent(String errorMessage) {
        super(errorMessage);
    }

    public boolean shouldUpdate() {
        return mShouldUpdate;
    }
}
