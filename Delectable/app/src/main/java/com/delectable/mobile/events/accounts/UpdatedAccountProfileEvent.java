package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.AccountProfile;
import com.delectable.mobile.events.BaseEvent;

public class UpdatedAccountProfileEvent extends BaseEvent {

    private AccountProfile mAccount;

    /**
     * The event broadcasts with only the account id if there was an error.
     */
    private String mAccountId;

    public UpdatedAccountProfileEvent(AccountProfile account) {
        super(true);
        mAccount = account;
        mAccountId = account.getId();
    }

    public UpdatedAccountProfileEvent(String accountId, String errorMessage) {
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
