package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.Account;

public class AssociateTwitterEvent extends BaseEvent {

    private Account mAccount;

    public AssociateTwitterEvent(Account account) {
        super(true);
        mAccount = account;
    }

    public AssociateTwitterEvent(String errorMessage) {
        super(errorMessage);
    }

    public Account getAcount() {
        return mAccount;
    }
}
