package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.events.BaseEvent;

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
