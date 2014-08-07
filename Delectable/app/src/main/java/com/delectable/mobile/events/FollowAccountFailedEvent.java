package com.delectable.mobile.events;

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
