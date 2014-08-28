package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.events.BaseEvent;

public class UpdatedAccountEvent extends BaseEvent {

    private Account mAccount;

    public  UpdatedAccountEvent(Account account) {
        super(true);
        mAccount = account;
    }

    public UpdatedAccountEvent(String errorMessage) {
        super(errorMessage);
    }

    public Account getAcount() {
        return mAccount;
    }
}
