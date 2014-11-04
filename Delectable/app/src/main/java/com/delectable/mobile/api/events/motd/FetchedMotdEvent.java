package com.delectable.mobile.api.events.motd;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.Motd;

public class FetchedMotdEvent extends BaseEvent {

    private Motd mMotd;

    public FetchedMotdEvent(Motd motd) {
        super(true);
        mMotd = motd;
    }

    public FetchedMotdEvent(String errorMessage) {
        super(errorMessage);
    }

    public FetchedMotdEvent(boolean isSuccessful) {
        super(isSuccessful);
    }

    public Motd getMotd() {
        return mMotd;
    }
}
