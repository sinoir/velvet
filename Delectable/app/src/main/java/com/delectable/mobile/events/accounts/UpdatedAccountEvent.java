package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.events.BaseEvent;

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
