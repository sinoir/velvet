package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.Account;

/**
 * Lets subscribers know jwhen the signed in user's account details have been updated.
 */
public class UpdatedAccountEvent extends BaseEvent {

    private Account mAccount;

    private String mRequestId = "";

    /**
     * Success event.
     */
    public UpdatedAccountEvent(String requestId, Account account) {
        super(true);
        mRequestId = requestId;
        mAccount = account;
    }

    /**
     * Fail event.
     */
    public UpdatedAccountEvent(String requestId, Account account, String errorMessage) {
        super(errorMessage);
        mAccount = account;
        mRequestId = requestId;
    }

    public Account getAccount() {
        return mAccount;
    }

    public String getRequestId() {
        return mRequestId;
    }
}
