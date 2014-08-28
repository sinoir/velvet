package com.delectable.mobile.events.accounts;

public class oldUpdatedAccountEvent {

    private String mAccountId;

    public oldUpdatedAccountEvent(String id) {
        mAccountId = id;
    }

    public String getAccountId() {
        return mAccountId;
    }

}
