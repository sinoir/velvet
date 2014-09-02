package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.events.BaseEvent;

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
