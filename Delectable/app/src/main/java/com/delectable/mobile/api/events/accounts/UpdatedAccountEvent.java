package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.Account;

public class UpdatedAccountEvent extends BaseEvent {

    private Account mAccount;

    /**
     * The event broadcasts with only the account id if there was an error.
     */
    private String mAccountId;

    private String mRequestId = "";

    public UpdatedAccountEvent(String requestId, Account account) {
        this(account);
        mRequestId = requestId;
    }

    public UpdatedAccountEvent(Account account) {
        super(true);
        mAccount = account;
        mAccountId = account.getId();
    }
    public UpdatedAccountEvent(String requestId, String accountId, String errorMessage) {
        this(accountId, errorMessage);
        mRequestId = requestId;
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

    public String getRequestId() {
        return mRequestId;
    }
}
