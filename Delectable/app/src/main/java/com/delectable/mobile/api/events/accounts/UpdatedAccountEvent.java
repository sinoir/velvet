package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.Account;

public class UpdatedAccountEvent extends BaseEvent {

    private Account mAccount;

    /**
     * The event broadcasts with only the account id if there was an error.
     */
    private String mAccountId;

    public UpdatedAccountEvent(Account account) {
        super(true);
        mAccount = account;
        mAccountId = account.getId();
    }

    public UpdatedAccountEvent(String accountId, String errorMessage) {
        super(errorMessage);
        mAccountId = accountId;
    }

    public Account getAccount() {
        return mAccount;
    }

    public String getAccountId() {
        return mAccountId;
    }
}
