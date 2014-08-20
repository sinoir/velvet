package com.delectable.mobile.events.captures;

import com.delectable.mobile.events.BaseEvent;

public class UpdatedFollowerFeedEvent extends BaseEvent {

    public UpdatedFollowerFeedEvent(boolean isSuccessful) {
        super(isSuccessful);
    }

    public UpdatedFollowerFeedEvent(String errorMessage) {
        super(errorMessage);
    }
}
