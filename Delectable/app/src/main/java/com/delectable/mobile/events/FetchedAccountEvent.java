package com.delectable.mobile.events;

public class FetchedAccountEvent {

    private String mAccountId;

    public FetchedAccountEvent(String id) {
        mAccountId = id;
    }

    public String getAccountId() {
        return mAccountId;
    }

}
