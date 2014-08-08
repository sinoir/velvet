package com.delectable.mobile.events.accounts;

public class FetchAccountFailedEvent {

    private String mAccountId;
    // TODO add error cause or message

    public FetchAccountFailedEvent(String id) {
        mAccountId = id;
    }

    public String getAccountId() {
        return mAccountId;
    }

}
