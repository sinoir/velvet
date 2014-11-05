package com.delectable.mobile.api.events.wines;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class FetchedWineSourceEvent extends BaseEvent {

    private String mWineId;

    public FetchedWineSourceEvent(String wineId) {
        super(true);
        mWineId = wineId;
    }

    public FetchedWineSourceEvent(String errorMessage, ErrorUtil errorCode, String wineId) {
        super(errorMessage, errorCode);
        mWineId = wineId;
    }

    public String getWineId() {
        return mWineId;
    }
}
