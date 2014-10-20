package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.Account;

public class AssociateFacebookEvent extends BaseEvent {

    private Account mAccount;

    public AssociateFacebookEvent(Account account) {
        super(true);
        mAccount = account;
    }

    public AssociateFacebookEvent(String errorMessage) {
        super(errorMessage);
    }

    public Account getAcount() {
        return mAccount;
    }
}
