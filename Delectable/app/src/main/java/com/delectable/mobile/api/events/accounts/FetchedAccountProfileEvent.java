package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.models.AccountProfile;
import com.delectable.mobile.api.events.BaseEvent;

public class FetchedAccountProfileEvent extends BaseEvent {

    private AccountProfile mAccount;

    /**
     * The event broadcasts with only the account id if there was an error.
     */
    private String mAccountId;

    public FetchedAccountProfileEvent(AccountProfile account) {
        super(true);
        mAccount = account;
        mAccountId = account.getId();
    }

    public FetchedAccountProfileEvent(String accountId, String errorMessage) {
        super(errorMessage);
        mAccountId = accountId;
    }

    public AccountProfile getAccount() {
        return mAccount;
    }

    public String getAccountId() {
        return mAccountId;
    }
}
