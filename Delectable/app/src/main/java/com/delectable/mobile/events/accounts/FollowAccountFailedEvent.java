package com.delectable.mobile.events.accounts;

public class FollowAccountFailedEvent {

    private String mAccountId;
    // TODO add error cause or message

    public FollowAccountFailedEvent(String id) {
        mAccountId = id;
    }

    public String getAccountId() {
        return mAccountId;
    }

}
