package com.delectable.mobile.events.captures;

import com.delectable.mobile.events.BaseEvent;

public class UpdatedUserCaptureFeedEvent extends BaseEvent {

    private String mAccountId;

    public UpdatedUserCaptureFeedEvent(boolean isSuccessful, String accountId) {
        super(isSuccessful);
        mAccountId = accountId;
    }

    public UpdatedUserCaptureFeedEvent(String errorMessage, String accountId) {
        super(errorMessage);
        mAccountId = accountId;
    }

    public String getAccountId() {
        return mAccountId;
    }

}
